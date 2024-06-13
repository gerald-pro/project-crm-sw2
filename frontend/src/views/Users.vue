<script setup lang="ts">
import { ALL_USERS } from '@/graphql/queries/userQueries';
import { useQuery } from '@vue/apollo-composable';
import { computed } from 'vue';


const { result, error, loading } = useQuery(ALL_USERS);

const users = computed(() => {
  console.log(result.value?.getAllUsers);
  return result.value?.getAllUsers || []; // Handle potential nullish values
});

</script>

<template>
  <div>
    <h3 class="text-3xl font-medium text-gray-700">
      Usuarios
    </h3>
    <div class="flex flex-col mt-6">
      <div class="py-2 -my-2 overflow-x-auto sm:-mx-6 sm:px-6 lg:-mx-8 lg:px-8">
        <div class="inline-block min-w-full overflow-hidden align-middle border-b border-gray-200 shadow sm:rounded-lg">
          <table class="min-w-full">
            <thead>
              <tr>
                <th
                  class="px-6 py-3 text-xs font-medium leading-4 tracking-wider text-left text-gray-500 uppercase bg-gray-100 border-b border-gray-200">
                  Nombre
                </th>

                <th
                  class="px-6 py-3 text-xs font-medium leading-4 tracking-wider text-left text-gray-500 uppercase bg-gray-100 border-b border-gray-200">
                  Username
                </th>
                <th
                  class="px-6 py-3 text-xs font-medium leading-4 tracking-wider text-left text-gray-500 uppercase bg-gray-100 border-b border-gray-200">
                  Email
                </th>
                <th
                  class="px-6 py-3 text-xs font-medium leading-4 tracking-wider text-left text-gray-500 uppercase bg-gray-100 border-b border-gray-200">
                  Email
                </th>
              </tr>
            </thead>

            <tbody class="bg-white">
              <tr v-for="user in users" :key="user.id">
                <td class="px-6 py-4 border-b border-gray-200 whitespace-nowrap">
                  <div class="flex items-center">
                    <div class="flex-shrink-0 w-10 h-10">
                      <img class="w-10 h-10 rounded-full"
                        src="https://images.unsplash.com/photo-1472099645785-5658abf4ff4e?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=facearea&facepad=2&w=256&h=256&q=80"
                        alt="profile pic">
                    </div>

                    <div class="ml-4">
                      <div class="text-sm font-medium leading-5 text-gray-900">
                        {{ user.name }}
                      </div>
                      <!-- <div class="text-sm leading-5 text-gray-500">
                        {{ user.password.slice(1, 5) + "***" }}
                      </div> -->
                    </div>
                  </div>
                </td>


                <td class="px-6 py-4 border-b border-gray-200 whitespace-nowrap">
                  <span
                    class="inline-flex px-2 text-xs font-semibold leading-5 text-green-800 bg-green-100 rounded-full">
                    {{ user.username }}</span>
                </td>

                <td class="px-6 py-4 text-sm leading-5 text-gray-500 border-b border-gray-200 whitespace-nowrap">
                  {{ user.email }}
                </td>

                <td class="px-6 py-4 text-sm leading-5 text-gray-500 border-b border-gray-200 whitespace-nowrap">
                  {{ user.createdAt }}
                </td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>
    </div>
  </div>
</template>