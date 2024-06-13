<script setup lang="ts">
import { computed, ref } from 'vue';
import BaseModal from '@/components/BaseModal.vue';
import LeadCard from '@/components/LeadCard.vue';
import { CREATE_LEAD, UPDATE_LEAD, DELETE_LEAD } from '@/graphql/mutations/leadMutations';
import { GET_LEADS } from '@/graphql/queries/leadQueries';
import { useMutation, useQuery } from '@vue/apollo-composable';
import type { Contact, Lead } from '@/graphql/interfaces';
import { toast } from 'vue3-toastify';
import { ALL_USERS } from '@/graphql/queries/userQueries';
import { GET_CONTACTS } from '@/graphql/queries/contactQueries';

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

const createLeadInput = ref({
  title: "",
  description: "",
  userId: "",
  contactId: "",
  value: 0,
  status: "nuevo",
});

const editLeadInput = ref({
  id: "",
  title: "",
  description: "",
  userId: "",
  contactId: "",
  value: 0,
  status: "",
});


// Funciones para abrir y cerrar modales
const openCreateModal = () => {
  isCreateModalOpen.value = true;
  createLeadInput.value = {
    title: "",
    description: "",
    userId: "",
    contactId: "",
    value: 0,
    status: "nuevo",
  }
};
const closeCreateModal = () => { isCreateModalOpen.value = false };

const openEditModal = (lead: Lead) => {
  console.log(lead);
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
    const { title, description, userId, contactId, value, status } = createLeadInput.value;

    await createLead({
      title, description, userId, contactId, value, status
    });

    toast.success("Lead creado exitosamente !");
    refetch();

  } catch (error: any) {
    errorMessage.value = error.message || 'Ocurrió un error durante la creacion del lead.';

    if (errorMessage.value) {
      toast.error(errorMessage.value);
    }
  } finally {
    isLoading.value = false;
    closeCreateModal();
  }
};

const handleUpdateLead = async () => {
  try {
    const { id, title, description, userId, contactId, value, status } = editLeadInput.value;
    console.log({ id, title, description, userId, contactId, value, status });
    await updateLead({
      id, title, description, userId, contactId, value, status
    });

    toast.success("Lead actualizado exitosamente !");
    refetch();

  } catch (error: any) {
    errorMessage.value = error.message || 'Ocurrió un error durante la actualización del lead.';

    if (errorMessage.value) {
      toast.error(errorMessage.value);
    }

  } finally {
    isLoading.value = false;
    closeEditModal();
  }

};

const handleDeleteLead = async (id: string) => {
  try {
    await deleteLead({ id });
    refetch();
    toast.success("Lead eliminado exitosamente!");
  } catch (error: any) {
    toast.error(error.message || 'Ocurrió un error durante la eliminación del lead.');
  }
};

// Consulta para obtener los leads
const { result: leadsResult, error: leadsError, refetch } = useQuery(GET_LEADS, () => ({
  page: currentPage.value,
  size: pageSize,
}), {
  fetchPolicy: 'network-only'
});

const { result: usersResult, error: usersError, loading: usersLoading } = useQuery(ALL_USERS);
const { result: contactsResult } = useQuery(GET_CONTACTS, () => ({
  page: 1,
  size: 20,
}));

const leads = computed<Lead[]>(() => leadsResult.value?.getLeads.leads || []);
const totalCount = computed(() => leadsResult.value?.getLeads.totalCount || 0);
const totalPages = computed(() => leadsResult.value?.getLeads.totalPages || 1);
const users = computed(() => usersResult.value?.getAllUsers || []);
const contacts = computed<Contact[]>(() => contactsResult.value?.getContacts.contacts || []);

interface GroupedLeads {
  [key: string]: Lead[];
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

  <BaseModal :open="isCreateModalOpen" @close="closeCreateModal">
    <template v-slot:title>
      <h3 class="text-2xl font-medium text-gray-700">
        Crear lead
      </h3>
    </template>
    <template v-slot:body>
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

        <div class="flex flex-col">
          <label for="create-contact">Contacto:</label>
          <select v-model="createLeadInput.contactId" id="create-contact"
            class="px-4 py-2 border rounded-md focus:ring focus:ring-opacity-50 focus:ring-indigo-300" required>
            <option value="">Seleccionar</option>
            <option v-for="contact in contacts" :key="contact.id" :value="contact.id">{{ contact.name }}</option>
          </select>
        </div>

        <div class="flex flex-col">
          <label for="create-address">Estado:</label>

          <select v-model="createLeadInput.status" id="create-value" type="text"
            class="px-4 py-2 border rounded-md focus:ring focus:ring-opacity-50 focus:ring-indigo-300">
            <option value="nuevo">Nuevo</option>
            <option value="calificado">Calificado</option>
            <option value="en_curso">En curso</option>
            <option value="oportunidad">Oportunidad</option>
            <option value="completado">Completado</option>
            <option value="cancelado">Cancelado</option>
          </select>
        </div>

        <div class="flex flex-col">
          <label for="edit-address">Valor proyectado:</label>
          <input v-model="createLeadInput.value" id="edit-value" type="number"
            class="px-4 py-2 border rounded-md focus:ring focus:ring-opacity-50 focus:ring-indigo-300" />
        </div>

        <div class="flex flex-col">
          <label for="edit-phone">Vendedor:</label>

          <select v-model="createLeadInput.userId" id="edit-value"
            class="px-4 py-2 border rounded-md focus:ring focus:ring-opacity-50 focus:ring-indigo-300" required>
            <option value="">Seleccionar</option>
            <option v-for="user in users" :key="user.id" :value="user.id">{{ user.name }}</option>
          </select>
        </div>

        <div class="flex justify-end pt-2">
          <button @click="closeCreateModal"
            class="px-6 py-2 mr-2 text-indigo-500 bg-transparent rounded-lg hover:bg-gray-100 hover:text-indigo-400 focus:outline-none">
            Cancelar
          </button>

          <button type="submit"
            class="px-6 py-2 font-medium text-white bg-indigo-600 rounded-md hover:bg-indigo-500 focus:outline-none">
            Guardar
          </button>
        </div>
      </form>
    </template>
  </BaseModal>

  <!-- Modal para editar lead -->
  <BaseModal :open="isEditModalOpen" @close="closeEditModal">
    <template v-slot:title>
      <h3 class="text-2xl font-medium text-gray-700">
        Editar lead
      </h3>
    </template>
    <template v-slot:body>
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
            <option value="nuevo">Nuevo</option>
            <option value="calificado">Calificado</option>
            <option value="en_curso">En curso</option>
            <option value="oportunidad">Oportunidad</option>
            <option value="completado">Completado</option>
            <option value="cancelado">Cancelado</option>
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
          <button @click="closeEditModal"
            class="px-6 py-2 mr-2 text-indigo-500 bg-transparent rounded-lg hover:bg-gray-100 hover:text-indigo-400 focus:outline-none">
            Cancelar
          </button>

          <button type="submit"
            class="px-6 py-2 font-medium text-white bg-indigo-600 rounded-md hover:bg-indigo-500 focus:outline-none">
            Guardar
          </button>
        </div>
      </form>
    </template>
  </BaseModal>
</template>