<script setup lang="ts">
import { useQuery } from '@vue/apollo-composable';
import { computed, ref, watch } from 'vue';
import { GET_SALES } from '@/graphql/queries/saleDetailQueries';


const currentPage = ref(1);
const pageSize = 10;

const { result, error, loading, refetch } = useQuery(GET_SALES, () => ({
  page: currentPage.value,
  size: pageSize,
}), {
  fetchPolicy: 'network-only' // Esto asegura que los datos siempre se obtengan de la red y no de la caché
});

const paginatedItems = computed(() => result.value?.getSalesDetails.salesDetails || []);
const totalCount = computed(() => result.value?.getSalesDetails.totalCount || 0);
const totalPages = computed(() => result.value?.getSalesDetails.totalPages || 1);

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
</script>

<template>
  <div>
    <h3 class="text-3xl font-medium text-gray-700">
      Detalles de ventas  <span class="text-xl">({{ totalCount }} registros)</span>
    </h3>
    <div class="flex flex-col mt-6">
      <div class="py-2 -my-2 overflow-x-auto sm:-mx-6 sm:px-6 lg:-mx-8 lg:px-8">
        <div class="inline-block min-w-full overflow-hidden align-middle border-b border-gray-200 shadow sm:rounded-lg">
          <table class="min-w-full">
            <thead>
              <tr>
                <th
                  class="px-6 py-3 text-xs font-medium leading-4 tracking-wider text-left text-gray-500 uppercase bg-gray-100 border-b border-gray-200">
                  Factura
                </th>
                <th
                  class="px-6 py-3 text-xs font-medium leading-4 tracking-wider text-left text-gray-500 uppercase bg-gray-100 border-b border-gray-200">
                  Fecha
                </th>
                <th
                  class="px-6 py-3 text-xs font-medium leading-4 tracking-wider text-left text-gray-500 uppercase bg-gray-100 border-b border-gray-200">
                  Producto
                </th>
                <th
                  class="px-6 py-3 text-xs font-medium leading-4 tracking-wider text-left text-gray-500 uppercase bg-gray-100 border-b border-gray-200">
                  Cantidad
                </th>
                <th
                  class="px-6 py-3 text-xs font-medium leading-4 tracking-wider text-left text-gray-500 uppercase bg-gray-100 border-b border-gray-200">
                  Pais
                </th>
              </tr>
            </thead>

            <tbody class="bg-white">

              <tr v-if="loading">
                <td colspan="5" class="px-4 py-5 text-center text-gray-600"> Cargando productos...</td>
              </tr>
              <tr v-else-if="error">
                <td colspan="5" class="px-4 py-5 text-center text-red-700"> Ha ocurrido un error: {{ error.message }}.
                  Por favor, intenta nuevamente.</td>
              </tr>
              <tr v-else v-for="sale in paginatedItems" :key="sale.id">
                <td class="px-6 py-4 border-b border-gray-200 whitespace-nowrap">
                  <span
                    class="inline-flex px-2 text-xs font-semibold leading-5 text-green-800 bg-green-100 rounded-full">
                    {{ sale.invoiceNo }}</span>
                </td>
                <td class="px-6 py-4 text-sm leading-5 text-gray-800 border-b border-gray-200 whitespace-nowrap">
                  {{ sale.invoiceDate }}
                </td>
                <td class="px-6 py-4 text-sm leading-5 text-gray-800 border-b border-gray-200 whitespace-nowrap">
                  {{ sale.description }}
                </td>
                <td class="px-6 py-4 text-sm leading-5 text-gray-800 border-b border-gray-200 whitespace-nowrap">
                  {{ sale.quantity }}
                </td>
                <td class="px-6 py-4 text-sm leading-5 text-gray-800 border-b border-gray-200 whitespace-nowrap">
                  {{ sale.country }}
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

        <span class="px-4">Pagina {{ currentPage + 1 }} de {{ totalPages }}</span>

        <button @click="nextPage" :disabled="currentPage === totalPages"
          class="px-4 py-2 text-sm font-medium text-gray-700 bg-white border border-gray-300 rounded-md shadow-sm hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500">
          Siguiente
        </button>
      </div>
    </div>
  </div>
</template>