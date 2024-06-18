<script setup lang="ts">
import { computed, reactive, ref } from 'vue';
import LeadCard from '@/components/LeadCard.vue';
import { CREATE_LEAD, UPDATE_LEAD, DELETE_LEAD } from '@/graphql/mutations/leadMutations';
import { GET_LEADS } from '@/graphql/queries/leadQueries';
import { useMutation, useQuery } from '@vue/apollo-composable';
import { LeadStatus, type Contact, type Lead, type LeadInput, type UpdateLeadInput } from '@/graphql/interfaces';
import { GET_CONTACTS } from '@/graphql/queries/contactQueries';
import ConfirmDialog from 'primevue/confirmdialog';
import { useConfirm } from 'primevue/useconfirm';
import { useToast } from 'primevue/usetoast';
import type { User } from '@/types/types';
import { GET_USER, GET_USERS } from '@/graphql/queries/userQueries';

const leadStatuses = [
  'nuevo',
  'calificado',
  'en_curso',
  'oportunidad',
  'completado',
  'cancelado',
];

const currentPage = ref(1);
const pageSize = 20;

const isCreateModalOpen = ref(false);
const isEditModalOpen = ref(false);

const createLeadInput = ref<LeadInput>({
  title: "",
  description: "",
  userId: "",
  contactId: "",
  value: 0,
  status: "nuevo",
});

const editLeadInput = ref<UpdateLeadInput>({
  id: ""
});


const confirm = useConfirm();
const toast = useToast();

// Funciones para abrir y cerrar modales
const openCreateModal = () => {
  isCreateModalOpen.value = true;

  createLeadInput.value = {
    title: "",
    description: "",
    userId: "",
    contactId: "",
    value: 0,
    status: LeadStatus.nuevo,
  }

  selectedContact.value = null;
  searchedContact.value = "";

};
const closeCreateModal = () => { isCreateModalOpen.value = false };

const openEditModal = (lead: Lead) => {
  isEditModalOpen.value = true
  editLeadInput.value = {
    id: lead.id,
    title: lead.title,
    description: lead.description ?? "",
    userId: lead.userId,
    contactId: lead.contactId,
    value: lead.value,
    status: lead.status,
  };


}

const closeEditModal = () => {
  isEditModalOpen.value = false
  editLeadInput.value = {
    id: "",
    title: "",
    description: "",
    userId: "",
    contactId: "",
    value: 0,
    status: "",
  };
}

const errorMessage = ref<string | null>(null);
const isLoading = ref(false);

// Mutaciones y manejo de la creación, actualización y eliminación de leads
const { mutate: createLead } = useMutation(CREATE_LEAD);
const { mutate: updateLead } = useMutation(UPDATE_LEAD);
const { mutate: deleteLead } = useMutation(DELETE_LEAD);

const handleCreateLead = async () => {
  try {
    if (selectedContact.value?.id != null) {
      createLeadInput.value.contactId = selectedContact.value.id;

      if (selectedUser.value?.id != null) {

        createLeadInput.value.userId = selectedUser.value.id;

        const { title, description, userId, contactId, value, status } = createLeadInput.value;

        await createLead({
          title, description, userId, contactId, value, status
        });

        toast.add({ severity: 'success', summary: 'Registrado', detail: 'Lead creado correctamente', life: 3000 });
        refetch();
        closeCreateModal();

        selectedContact.value = null;
        searchedContact.value = "";

      } else {
        toast.add({ severity: 'error', summary: 'Error', detail: "Debe seleccionar un vendedor valido", life: 5000 });
      }

    } else {
      toast.add({ severity: 'error', summary: 'Error', detail: "Debe seleccionar un contacto valido", life: 5000 });

    }

    /* 
        */

  } catch (error: any) {
    errorMessage.value = error.message || 'Ocurrió un error durante la creacion del lead.';
    if (errorMessage.value) {
      toast.add({ severity: 'error', summary: 'Error', detail: errorMessage.value, life: 5000 });
    }

  } finally {
    isLoading.value = false;
  }
};

const handleUpdateLead = async () => {
  try {
    const { id, title, description, userId, contactId, value, status } = editLeadInput.value;
    console.log({ id, title, description, userId, contactId, value, status });
    await updateLead({
      id, title, description, userId, contactId, value, status
    });

    toast.add({ severity: 'success', summary: 'Registrado', detail: 'Lead actualizado exitosamente!', life: 3000 });
    refetch();
    closeEditModal();
  } catch (error: any) {
    errorMessage.value = error.message || 'Ocurrió un error durante la actualización del lead.';
    if (errorMessage.value) {
      toast.add({ severity: 'error', summary: 'Error', detail: errorMessage.value, life: 5000 });
    }

  } finally {
    isLoading.value = false;
  }

};


const handleDeleteLead = (id: number) => {
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
        await deleteLead({ id: id });
        refetch()
        toast.add({ severity: 'success', summary: 'Eliminado', detail: 'Recurso eliminado correctamente', life: 3000 });
      } catch (error) {
        toast.add({ severity: 'error', summary: 'Error', detail: error, life: 5000 });
      }
    },
  });
};


const searchedContact = ref<string>();
const selectedContact = ref<Contact | null>(null);

const searchedUser = ref<string>();
const selectedUser = ref<User>();

// Consulta para obtener los leads
const { result: leadsResult, error: leadsError, refetch } = useQuery(GET_LEADS, () => ({
  page: currentPage.value,
  size: pageSize,
}), {
  fetchPolicy: 'network-only'
});


const { result: usersResult, refetch: refetchUsers } = useQuery(GET_USERS, () => ({
  page: 1,
  size: 2500,
  filter: searchedUser.value,
}));

const { result: contactsResult, refetch: refetchContacts } = useQuery(GET_CONTACTS, () => ({
  page: 1,
  size: 2500,
  filter: searchedContact.value,
}));


const leads = computed<Lead[]>(() => leadsResult.value?.getLeads.leads || []);
const totalCount = computed(() => leadsResult.value?.getLeads.totalCount || 0);
const totalPages = computed(() => leadsResult.value?.getLeads.totalPages || 1);
const users = computed(() => usersResult.value?.getUsers.users || []);
let contacts = computed<Contact[]>(() => contactsResult.value?.getContacts.contacts || []);

interface GroupedLeads {
  [key: string]: Lead[];
}

const searchContact = (event: any) => {
  setTimeout(async () => {

    if (!event.query.trim().length) {
      searchedContact.value = "";
      await refetchContacts()
    } else {
      searchedContact.value = event.query.toLowerCase();
      await refetchContacts()
    }
  }, 1000);
}

const searchUser = (event: any) => {
  setTimeout(async () => {

    if (!event.query.trim().length) {
      searchedUser.value = "";
      await refetchUsers()
    } else {
      searchedUser.value = event.query.toLowerCase();
      await refetchUsers()
    }
  }, 1000);
}

const leadsByStatus = computed<GroupedLeads>(() => {
  const grouped: GroupedLeads = {};
  leadStatuses.forEach((status) => {
    grouped[status] = leads.value.filter((lead) => lead.status === status);
  });

  return grouped;
});
</script>


<template>
  <Toast />
  <ConfirmDialog />
  <div class="flex justify-start">
    <h3 class="text-3xl font-medium text-gray-700">
      Leads <span class="text-xl">({{ totalCount }} registros) </span>
    </h3>

    <button @click="openCreateModal"
      class="px-6 py-2 font-medium text-white bg-indigo-600 rounded-md hover:bg-indigo-500 focus:outline-none">Crear</button>

  </div>
  <div class="p-4 bg-white shadow sm:rounded-l mt-6 overflow-auto">
    <div class="flex space-x-6">
      <div v-for="status in leadStatuses" :key="status" class=" w-1/5 capitalize flex-shrink-0">
        <h2 class="text-xl font-semibold mb-4">{{ status }}</h2>
        <div>
          <LeadCard v-for="lead in leadsByStatus[status]" :key="lead.id" :lead="lead" @editLead="openEditModal"
            @deleteLead="handleDeleteLead" />
        </div>
      </div>
    </div>
  </div>
  <Dialog v-model:visible="isCreateModalOpen" modal header="Crear lead" :style="{ width: '25rem' }">
    <form @submit.prevent="handleCreateLead">

      <div class="flex flex-col">
        <label for="create-name">Titulo:</label>
        <input v-model="createLeadInput.title" id="create-title" type="text"
          class="px-4 py-2 border rounded-md focus:ring focus:ring-opacity-50 focus:ring-indigo-300" required />
      </div>

      <div class="flex flex-col">
        <label for="create-email">Descripción:</label>
        <input v-model="createLeadInput.description" id="create-description" type="text"
          class="px-4 py-2 border rounded-md focus:ring focus:ring-opacity-50 focus:ring-indigo-300" required />
      </div>

      <div class="flex flex-col w-full">
        <label for="create-contact">Contacto:</label>
        <AutoComplete style="width: 400px;" v-model="selectedContact" optionLabel="name" :suggestions="contacts"
          @complete="searchContact" @select="selectedContact" />
      </div>

      <div class="flex flex-col">
        <label for="create-address">Estado:</label>

        <select v-model="createLeadInput.status" id="create-value" type="text"
          class="px-4 py-2 border rounded-md focus:ring focus:ring-opacity-50 focus:ring-indigo-300">
          <option :value="LeadStatus.nuevo">Nuevo</option>
          <option :value="LeadStatus.calificado">Calificado</option>
          <option :value="LeadStatus.en_curso">En curso</option>
          <option :value="LeadStatus.oportunidad">Oportunidad</option>
          <option :value="LeadStatus.completado">Completado</option>
          <option :value="LeadStatus.cancelado">Cancelado</option>
        </select>
      </div>

      <div class="flex flex-col">
        <label for="edit-address">Valor proyectado:</label>
        <input v-model="createLeadInput.value" id="edit-value" type="number"
          class="px-4 py-2 border rounded-md focus:ring focus:ring-opacity-50 focus:ring-indigo-300" />
      </div>

      <div class="flex flex-col">
        <label for="edit-phone">Vendedor:</label>

        <AutoComplete v-model="selectedUser" optionLabel="name" :suggestions="users" @complete="searchUser"
          @select="selectedUser" />
      </div>

      <div class="flex justify-end pt-2">
        <button @click="closeCreateModal" type="button"
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

  <!-- Modal para editar lead -->
  <Dialog v-model:visible="isEditModalOpen" modal header="Editar lead" :style="{ width: '25rem' }">

    <form @submit.prevent="handleUpdateLead">

      <div class="flex flex-col">
        <label for="edit-name">Titulo:</label>
        <input v-model="editLeadInput.title" id="edit-name" type="text"
          class="px-4 py-2 border rounded-md focus:ring focus:ring-opacity-50 focus:ring-indigo-300" required />
      </div>

      <div class="flex flex-col">
        <label for="edit-email">Descripción:</label>
        <input v-model="editLeadInput.description" id="edit-description" type="text"
          class="px-4 py-2 border rounded-md focus:ring focus:ring-opacity-50 focus:ring-indigo-300" />
      </div>

      <div class="flex flex-col">
        <label for="edit-contact">Contacto:</label>
        <select v-model="editLeadInput.contactId" id="edit-contact"
          class="px-4 py-2 border rounded-md focus:ring focus:ring-opacity-50 focus:ring-indigo-300" required>
          <option value="">Seleccionar</option>
          <option v-for="contact in contacts" :key="contact.id" :value="contact.id">{{ contact.name }}</option>
        </select>
      </div>


      <div class="flex flex-col">
        <label for="create-address">Estado:</label>

        <select v-model="editLeadInput.status" id="create-value" type="text"
          class="px-4 py-2 border rounded-md focus:ring focus:ring-opacity-50 focus:ring-indigo-300">
          <option :value="LeadStatus.nuevo">Nuevo</option>
          <option :value="LeadStatus.calificado">Calificado</option>
          <option :value="LeadStatus.en_curso">En curso</option>
          <option :value="LeadStatus.oportunidad">Oportunidad</option>
          <option :value="LeadStatus.completado">Completado</option>
          <option :value="LeadStatus.cancelado">Cancelado</option>
        </select>
      </div>

      <div class="flex flex-col">
        <label for="edit-value">Valor proyectado:</label>
        <input v-model="editLeadInput.value" id="edit-value" type="number"
          class="px-4 py-2 border rounded-md focus:ring focus:ring-opacity-50 focus:ring-indigo-300" />
      </div>

      <div class="flex flex-col">
        <label for="edit-userId">Vendedor:</label>
        <select v-model="editLeadInput.userId" id="edit-userId"
          class="px-4 py-2 border rounded-md focus:ring focus:ring-opacity-50 focus:ring-indigo-300" required>
          <option value="">Seleccionar</option>
          <option v-for="user in users" :key="user.id" :value="user.id">{{ user.name }}</option>
        </select>


      </div>

      <div class="flex justify-end pt-2">
        <button @click="closeEditModal" type="button"
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

</template>