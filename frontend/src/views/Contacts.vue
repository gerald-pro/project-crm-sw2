<script setup lang="ts">
import { GET_CONTACTS } from '@/graphql/queries/contactQueries';
import BaseModal from '@/components/BaseModal.vue';
import { useMutation, useQuery } from '@vue/apollo-composable';
import { computed, ref } from 'vue';
import { CREATE_CONTACT, UPDATE_CONTACT } from '@/graphql/mutations/contactMutations';
import type { Contact } from '@/graphql/interfaces';
import { toast } from 'vue3-toastify';
import 'vue3-toastify/dist/index.css';


const currentPage = ref(1);
const pageSize = 10;
const filter = ref('');

const { result, error, loading, refetch } = useQuery(GET_CONTACTS, () => ({
  page: currentPage.value,
  size: pageSize,
  filter: filter.value,
}), {
  fetchPolicy: 'network-only' // Esto asegura que los datos siempre se obtengan de la red y no de la caché
});

const paginatedItems = computed<Contact[]>(() => result.value?.getContacts.contacts || []);
const totalCount = computed(() => result.value?.getContacts.totalCount || 0);
const totalPages = computed(() => result.value?.getContacts.totalPages || 1);

const nextPage = () => {
  if (currentPage.value < totalPages.value) {
    currentPage.value++;
    console.log("prox")
  }
};

const prevPage = () => {
  if (currentPage.value > 1) {
    currentPage.value--;
    console.log("prev")
  }
};


const isCreateModalOpen = ref(false)
const isEditModalOpen = ref(false)
const createContactInput = ref({
  name: '',
  email: '',
  phone: '',
  address: ''
});

const editContactInput = ref({
  id: '',
  name: '',
  email: '',
  phone: '',
  address: ''
});

const openCreateModal = () => {
  isCreateModalOpen.value = true
}

const closeCreateModal = () => {
  isCreateModalOpen.value = false
}

const openEditModal = (contact: Contact) => {
  isEditModalOpen.value = true
  editContactInput.value = {
    id: contact.id,
    name: contact.name,
    email: contact.email,
    phone: contact.phone ?? "",
    address: contact.address ?? ""
  };
}

const closeEditModal = () => {
  isEditModalOpen.value = false
  editContactInput.value = {
    id: '',
    name: '',
    email: '',
    phone: '',
    address: ''
  };
}

const errorMessage = ref<string | null>(null);
const isLoading = ref(false);

const { mutate: createContact } = useMutation(CREATE_CONTACT);
const { mutate: updateContact } = useMutation(UPDATE_CONTACT);

const handleCreateContact = async () => {
  try {
    const { name, email, phone, address } = createContactInput.value;

    await createContact({
      name,
      email,
      phone: phone || '',
      address: address || ''
    });

    toast.success("Contacto creado exitosamente !");

  } catch (error: any) {
    errorMessage.value = error.message || 'Ocurrió un error durante la creacion del contacto.';
    if (errorMessage.value) {
      toast.error(errorMessage.value);
    }
  } finally {
    isLoading.value = false;
    closeCreateModal();
  }
};

const handleUpdateContact = async () => {
  try {
    const { id, name, email, phone, address } = editContactInput.value;

    await updateContact({
      id, name, email, phone, address
    });

    toast.success("Contacto actualizado exitosamente !");
    refetch();
  } catch (error: any) {
    errorMessage.value = error.message || 'Ocurrió un error durante la actualización del contacto.';
    if (errorMessage.value) {
      toast.error(errorMessage.value);
    }
  } finally {
    isLoading.value = false;
    closeEditModal();
  }
};
</script>

<template>
  <div>
    <div class="row">
      <div class="flex justify-start">
        <h3 class="text-3xl font-medium text-gray-700">
          Contactos <span class="text-xl">({{ totalCount }} registros)</span>
        </h3>

        <button @click="openCreateModal"
          class="px-6 py-2 font-medium text-white bg-indigo-600 rounded-md hover:bg-indigo-500 focus:outline-none">Crear</button>

      </div>
      <div class="flex justify-end">
        <input v-model="filter" placeholder="Filtrar" type="text">
      </div>
    </div>
    <div class="flex flex-col mt-6">
      <div class="py-2 -my-2 overflow-x-auto sm:-mx-6 sm:px-6 lg:-mx-8 lg:px-8">
        <div class="inline-block min-w-full overflow-hidden align-middle border-b border-gray-200 shadow sm:rounded-lg">
          <table class="min-w-full">
            <thead>
              <tr>
                <th
                  class="px-6 py-3 text-xs font-medium leading-4 tracking-wider text-left text-gray-500 uppercase bg-gray-100 border-b border-gray-200">
                  ID
                </th>

                <th
                  class="px-6 py-3 text-xs font-medium leading-4 tracking-wider text-left text-gray-500 uppercase bg-gray-100 border-b border-gray-200">
                  Nombre
                </th>

                <th
                  class="px-6 py-3 text-xs font-medium leading-4 tracking-wider text-left text-gray-500 uppercase bg-gray-100 border-b border-gray-200">
                  Email
                </th>

                <th
                  class="px-6 py-3 text-xs font-medium leading-4 tracking-wider text-left text-gray-500 uppercase bg-gray-100 border-b border-gray-200">
                  Teléfono
                </th>

                <th
                  class="px-6 py-3 text-xs font-medium leading-4 tracking-wider text-left text-gray-500 uppercase bg-gray-100 border-b border-gray-200">
                  Dirección
                </th>

                <th
                  class="px-6 py-3 text-xs font-medium leading-4 tracking-wider text-left text-gray-500 uppercase bg-gray-100 border-b border-gray-200">
                  Registrado
                </th>

                <th
                  class="px-6 py-3 text-xs font-medium leading-4 tracking-wider text-left text-gray-500 uppercase bg-gray-100 border-b border-gray-200">
                  Acciones
                </th>
              </tr>
            </thead>

            <tbody class="bg-white">
              <tr v-if="loading">
                <td colspan="7" class="px-4 py-5 text-center text-gray-600"> Cargando...</td>
              </tr>
              <tr v-else-if="error">
                <td colspan="7" class="px-4 py-5 text-center text-red-700"> Ha ocurrido un error: {{ error.message }}.
                  Por favor, intenta nuevamente.</td>
              </tr>
              <tr v-else v-for="item in paginatedItems" :key="item.id">
                <td class="px-6 py-4 border-b border-gray-200 whitespace-nowrap">
                  <span
                    class="inline-flex px-2 text-xs font-semibold leading-5 text-green-800 bg-green-100 rounded-full">{{
                      item.id }}</span>
                </td>

                <td class="px-6 py-4 text-sm leading-5 text-gray-800 border-b border-gray-200 whitespace-nowrap">
                  {{ item.name }}
                </td>

                <td class="px-6 py-4 text-sm leading-5 text-gray-800 border-b border-gray-200 whitespace-nowrap">
                  {{ item.email }}
                </td>

                <td class="px-6 py-4 text-sm leading-5 text-gray-800 border-b border-gray-200 whitespace-nowrap">
                  {{ item.phone }}
                </td>

                <td class="px-6 py-4 text-sm leading-5 text-gray-800 border-b border-gray-200 whitespace-nowrap">
                  {{ item.address }}
                </td>

                <td class="px-6 py-4 text-sm leading-5 text-gray-800 border-b border-gray-200 whitespace-nowrap">
                  {{ item.createdAt }}
                </td>

                <td
                  class="px-6 py-4 text-sm font-medium leading-5 text-right border-b border-gray-200 whitespace-nowrap">
                  <button @click="openEditModal(item)" class="text-indigo-600 hover:text-indigo-900">Edit</button>
                </td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>

      <div class="mt-4">
        <button @click="prevPage" :disabled="currentPage === 1"
          class="px-4 py-2 mr-2 text-sm font-medium text-gray-700 bg-white border border-gray-300 rounded-md shadow-sm hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500">
          Anterior
        </button>

        <span class="px-4">Pagina {{ currentPage }} de {{ totalPages }}</span>

        <button @click="nextPage" :disabled="currentPage === totalPages"
          class="px-4 py-2 text-sm font-medium text-gray-700 bg-white border border-gray-300 rounded-md shadow-sm hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500">
          Siguiente
        </button>
      </div>
    </div>

    <BaseModal :open="isCreateModalOpen" @close="closeCreateModal">
      <template v-slot:title>
        <h3 class="text-2xl font-medium text-gray-700">
          Crear contacto
        </h3>
      </template>

      <template v-slot:body>
        <form @submit.prevent="handleCreateContact">
          <div class="flex flex-col">
            <label for="name">Nombre:</label>
            <input v-model="createContactInput.name" id="name" type="text"
              class="px-4 py-2 border rounded-md focus:ring focus:ring-opacity-50 focus:ring-indigo-300" required />
          </div>

          <div class="flex flex-col">
            <label for="email">Email:</label>
            <input v-model="createContactInput.email" id="email" type="email"
              class="px-4 py-2 border rounded-md focus:ring focus:ring-opacity-50 focus:ring-indigo-300" required />
          </div>

          <div class="flex flex-col">
            <label for="phone">Teléfono:</label>
            <input v-model="createContactInput.phone" id="phone" type="tel"
              class="px-4 py-2 border rounded-md focus:ring focus:ring-opacity-50 focus:ring-indigo-300" />
          </div>

          <div class="flex flex-col">
            <label for="address">Dirección:</label>
            <input v-model="createContactInput.address" id="address" type="text"
              class="px-4 py-2 border rounded-md focus:ring focus:ring-opacity-50 focus:ring-indigo-300" />
          </div>

          <div class="flex justify-end pt-2">
            <button @click="closeCreateModal"
              class="px-6 py-2 mr-2 text-indigo-500 bg-transparent rounded-lg hover:bg-gray-100 hover:text-indigo-400 focus:outline-none">Cancelar</button>
            <button type="submit"
              class="px-6 py-2 font-medium text-white bg-indigo-600 rounded-md hover:bg-indigo-500 focus:outline-none">Crear</button>
          </div>
        </form>
      </template>
    </BaseModal>

    <BaseModal :open="isEditModalOpen" @close="closeEditModal">
      <template v-slot:title>
        <h3 class="text-2xl font-medium text-gray-700">
          Editar contacto
        </h3>
      </template>

      <template v-slot:body>
        <form @submit.prevent="handleUpdateContact">

          <div class="flex flex-col">
            <label for="edit-name">Nombre:</label>
            <input v-model="editContactInput.name" id="edit-name" type="text"
              class="px-4 py-2 border rounded-md focus:ring focus:ring-opacity-50 focus:ring-indigo-300" required />
          </div>

          <div class="flex flex-col">
            <label for="edit-email">Email:</label>
            <input v-model="editContactInput.email" id="edit-email" type="email"
              class="px-4 py-2 border rounded-md focus:ring focus:ring-opacity-50 focus:ring-indigo-300" required />
          </div>

          <div class="flex flex-col">
            <label for="edit-phone">Teléfono:</label>
            <input v-model="editContactInput.phone" id="edit-phone" type="tel"
              class="px-4 py-2 border rounded-md focus:ring focus:ring-opacity-50 focus:ring-indigo-300" />
          </div>

          <div class="flex flex-col">
            <label for="edit-address">Dirección:</label>
            <input v-model="editContactInput.address" id="edit-address" type="text"
              class="px-4 py-2 border rounded-md focus:ring focus:ring-opacity-50 focus:ring-indigo-300" />
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
  </div>
</template>