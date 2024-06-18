<script setup lang="ts">
import { useMutation, useQuery } from '@vue/apollo-composable';
import { computed, onMounted, ref, watch } from 'vue';
import { useRoute } from 'vue-router';
import DataTable from 'primevue/datatable';
import Column from 'primevue/column';
import ConfirmDialog from 'primevue/confirmdialog';
import { useConfirm } from "primevue/useconfirm";
import { useToast } from "primevue/usetoast";
import { GET_LEAD } from '@/graphql/queries/leadQueries';
import { ActivityType, LeadStatus, type Activity, type ActivityInput, type Lead, type LeadInput, type UpdateActivityInput, type UpdateLeadInput } from '@/graphql/interfaces';
import { GET_ACTIVITIES_BY_LEAD } from '@/graphql/queries/activityQueries';
import { CREATE_ACTIVITY, DELETE_ACTIVITY, UPDATE_ACTIVITY } from '@/graphql/mutations/activityMutations';
import { UPDATE_LEAD } from '@/graphql/mutations/leadMutations';


const route = useRoute();
const leadId = ref(route.params.id as string);
const { result: leadResult, loading, error } = useQuery(GET_LEAD, { id: leadId });
const { result: activityResult, refetch } = useQuery(GET_ACTIVITIES_BY_LEAD, { id: leadId });
const selectedStatus = ref();

const lead = computed<Lead>(() => {
    console.log(leadResult.value?.getLead);
    selectedStatus.value = leadResult.value?.getLead.status; return leadResult.value?.getLead || []
});
const activities = computed<Activity[]>(() => { console.log(activityResult.value); return activityResult.value?.getActivitiesByLead || [] });


const { mutate: createActivityMutation } = useMutation(CREATE_ACTIVITY);
const { mutate: updateLeadMutation } = useMutation(UPDATE_LEAD);
const { mutate: deleteActivityMutation } = useMutation(DELETE_ACTIVITY);


const formatDate = (value: any) => {
    return new Date(value).toLocaleDateString('es-BO', {
        day: '2-digit',
        month: '2-digit',
        year: 'numeric',
        hour: '2-digit',
        minute: '2-digit'
    });
};
const updateLeadInput = ref<UpdateLeadInput>({
    id: ""
});

watch(selectedStatus, (newValue, oldValue) => {
    if (lead.value.title != "" && newValue != lead.value.status) {
        updateLeadInput.value.status = newValue;
        handleLeadUpdate();
    }

});


const confirm = useConfirm();
const toast = useToast();

watch(error, (errorValue) => {
    if (errorValue) {
        toast.add({ severity: 'error', summary: 'Error', detail: error, life: 5000 });
    }
});


const visibleCreateActDialog = ref(false);
const visibleUpdateActDialog = ref(false);

const createActivity = ref<ActivityInput>({ activityDate: "", contactId: "", leadId: "", title: "", type: ActivityType.CALL });


const openCreateActDialog = () => {
    visibleCreateActDialog.value = true;
}

const closeUpdateActDialog = () => {
    visibleUpdateActDialog.value = false;
}


const handleActivityCreate = async () => {
    try {
        createActivity.value.leadId = leadId.value;
        await createActivityMutation({ ...createActivity.value });
        createActivity.value = { activityDate: "", contactId: "", leadId: "", title: "", type: ActivityType.CALL };
        refetch()
        toast.add({ severity: 'success', summary: 'Registrado', detail: 'Actividad agendada correctamente', life: 3000 });
        visibleCreateActDialog.value = false;
    } catch (error) {
        toast.add({ severity: 'error', summary: 'Error', detail: error, life: 5000 });
    }
};

const handleLeadUpdate = async () => {
    try {
        updateLeadInput.value.id = leadId.value;
        await updateLeadMutation({ ...updateLeadInput.value });
        updateLeadInput.value = { id: "" };
        toast.add({ severity: 'success', summary: 'Registrado', detail: 'Lead actualizado correctamente', life: 3000 });
    } catch (error) {
        toast.add({ severity: 'error', summary: 'Error', detail: error, life: 5000 });
    }
};


const handleActivityDelete = (id: number) => {
    confirm.require({
        message: '¿Estas seguro? Esta acción es irreversible',
        header: 'Eliminar',
        rejectProps: {
            label: 'Cancelar',
            severity: 'secondary',
            outlined: true
        },
        acceptProps: {
            label: 'Eliminar',
            severity: 'danger',
        },
        accept: async () => {
            try {
                await deleteActivityMutation({ id: id });
                refetch()
                toast.add({ severity: 'success', summary: 'Eliminado', detail: 'Recurso eliminado correctamente', life: 3000 });
            } catch (error) {
                toast.add({ severity: 'error', summary: 'Error', detail: error, life: 5000 });
            }
        },
    });
};
</script>

<template>
    <div>
        <Toast />
        <ConfirmDialog />
        <div class=" bg-white shadow-lg rounded-lg overflow-hidden">
            <div class="px-6 py-3">

                <div class="flex justify-between text-gray-700">
                    <div>
                        <h1 class="text-xl font-bold ">{{ lead.title }}</h1>
                        <p class="font-medium ">Caso de estudio</p>
                    </div>
                    <div class="my-auto space-x-4">
<!-- 
                        <button label="Editar" outlined>
                            <font-awesome-icon icon="pen" />
                        </button>

                        <button label="Eliminar" outlined>
                            <font-awesome-icon icon="trash" />
                        </button> -->
                    </div>
                </div>

            </div>

            <div class="px-6 py-3">
                <DataTable :value="activities" stripedRows removableSort size="small" paginator :rows="10"
                    :loading="loading"
                    paginatorTemplate="RowsPerPageDropdown FirstPageLink PrevPageLink CurrentPageReport NextPageLink LastPageLink"
                    :globalFilterFields="['title', 'description']"
                    currentPageReportTemplate="{first} a {last} de {totalRecords} registros">
                    <template #loading> Cargando datos... </template>
                    <template #header>
                        <div class="flex flex-wrap items-center justify-between gap-2 -mt-1 mb-3">
                            <div class="relative flex flex-wrap">
                                <div class="grid w-[40rem] grid-cols-6 gap-2 rounded-lg bg-gray-200 p-1 text-sm">
                                    <div>
                                        <input type="radio" name="option" id="1" :value="LeadStatus.nuevo"
                                            class="peer hidden" v-model="selectedStatus" />
                                        <label for="1"
                                            class="block cursor-pointer select-none rounded-xl p-1 text-center peer-checked:bg-blue-500 peer-checked:font-bold peer-checked:text-white">Nuevo</label>
                                    </div>

                                    <div>
                                        <input type="radio" name="option" id="2" :value="LeadStatus.calificado"
                                            class="peer hidden" v-model="selectedStatus" />
                                        <label for="2"
                                            class="block cursor-pointer select-none rounded-xl p-1 text-center peer-checked:bg-blue-500 peer-checked:font-bold peer-checked:text-white">Calificado</label>
                                    </div>

                                    <div>
                                        <input type="radio" name="option" id="3" :value="LeadStatus.en_curso"
                                            class="peer hidden" v-model="selectedStatus" />
                                        <label for="3"
                                            class="block cursor-pointer select-none rounded-xl p-1 text-center peer-checked:bg-blue-500 peer-checked:font-bold peer-checked:text-white">En
                                            curso</label>
                                    </div>

                                    <div>
                                        <input type="radio" name="option" id="4" :value="LeadStatus.oportunidad"
                                            class="peer hidden" v-model="selectedStatus" />
                                        <label for="4"
                                            class="block cursor-pointer select-none rounded-xl p-1 text-center peer-checked:bg-blue-500 peer-checked:font-bold peer-checked:text-white">Oportunidad</label>
                                    </div>

                                    <div>
                                        <input type="radio" name="option" id="5" :value="LeadStatus.completado"
                                            class="peer hidden" v-model="selectedStatus" />
                                        <label for="5"
                                            class="block cursor-pointer select-none rounded-xl p-1 text-center peer-checked:bg-green-600 peer-checked:font-bold peer-checked:text-white">Completado</label>
                                    </div>

                                    <div>
                                        <input type="radio" name="option" id="6" :value="LeadStatus.cancelado"
                                            class="peer hidden" v-model="selectedStatus" />
                                        <label for="6"
                                            class="block cursor-pointer select-none rounded-xl p-1 text-center peer-checked:bg-red-500 peer-checked:font-bold peer-checked:text-white">Cancelado</label>
                                    </div>
                                </div>
                            </div>
                            <div class="flex flex-wrap space-x-4">
                                <button @click="visibleCreateActDialog = true"
                                    class="px-4 py-1 font-medium text-indigo-600  rounded hover:bg-gray-200 focus:outline-none border border-indigo-400">
                                    Nueva actividad
                                </button>
                            </div>
                        </div>
                    </template>
                    <Column field="title" header="Título" sortable></Column>
                    <Column field="description" header="Descripción" dataType="date"></Column>
                    <Column field="type" header="Tipo" sortable></Column>

                    <Column header="Fecha" sortable dataType="date">
                        <template #body="{ data }">
                            {{ formatDate(data.activityDate) }}
                        </template>
                    </Column>
                    <Column header="Opciones">
                        <template #body="{ data }">

                            <button label="Eliminar" outlined @click="handleActivityDelete(data.id)">
                                <font-awesome-icon icon="trash" />
                            </button>
                        </template>
                    </Column>
                </DataTable>
            </div>

            <Dialog v-model:visible="visibleCreateActDialog" modal header="Crear actividad" :style="{ width: '25rem' }">
                <form @submit.prevent="handleActivityCreate()" class="space-y-3">
                    <div class="flex flex-col">
                        <label for="create-name">Título:</label>
                        <input v-model="createActivity.title" id="create-title" type="text"
                            class="px-4 py-2 border rounded-md focus:ring focus:ring-opacity-50 focus:ring-indigo-300"
                            required />
                    </div>

                    <div class="flex flex-col">
                        <label for="create-email">Descripción:</label>
                        <input v-model="createActivity.description" id="create-description" type="text"
                            class="px-4 py-2 border rounded-md focus:ring focus:ring-opacity-50 focus:ring-indigo-300" />
                    </div>

                    <div class="flex flex-col">
                        <label for="create-name">Tipo:</label>

                        <select v-model="createActivity.type" name="create-title" id="create-title"
                            class="px-4 py-2 border rounded-md focus:ring focus:ring-opacity-50 focus:ring-indigo-300"
                            required>
                            <option :value="ActivityType.CALL">Llamada</option>
                            <option :value="ActivityType.MEETING">Reunión</option>
                            <option .value="ActivityType.LUNCH">Almuerzo</option>
                        </select>
                    </div>

                    <div class="flex flex-col">
                        <label for="create-activityDate">Fecha:</label>
                        <input v-model="createActivity.activityDate" id="create-activityDate" type="datetime-local"
                            class="px-4 py-2 border rounded-md focus:ring focus:ring-opacity-50 focus:ring-indigo-300"
                            required />
                    </div>

                    <div class="flex justify-end pt-2">
                        <button type="button" @click="visibleCreateActDialog = false"
                            class="px-6 py-2 mr-2 text-indigo-500 bg-transparent rounded-lg hover:bg-gray-100 hover:text-indigo-400 focus:outline-none">
                            Cancelar
                        </button>

                        <button type="submit"
                            class="px-6 py-2 font-medium text-white bg-indigo-600 rounded-md hover:bg-indigo-500 focus:outline-none">
                            Guardar
                        </button>
                    </div>
                </form>
            </Dialog>
        </div>
    </div>
</template>