<script setup lang="ts">
import { ref } from 'vue';
import { useUserStore } from '@/stores/user';
import { useMutation } from '@vue/apollo-composable';
import { USER_SIGNUP } from '@/graphql/mutations/userMutations';
import type { FetchResult } from '@apollo/client';
import router from '@/router';

const userStore = useUserStore();

const signUpDetails = ref({
  name: '',
  username: '',
  email: '',
  password: ''
});

const errorMessage = ref<string | null>(null);
const isLoading = ref(false);
const { mutate: registerUser } = useMutation(USER_SIGNUP);


const userSignUp = async () => {
  try {
    const result: FetchResult<any, Record<string, any>, Record<string, any>> | null = await registerUser({
      name: signUpDetails.value.name,
      username: signUpDetails.value.username,
      password: signUpDetails.value.password,
      email: signUpDetails.value.email,
    });

    if (result && result.data) {
      userStore.setToken(result.data.register.token);
      userStore.setUser(result.data.register.user);
    } else {
      console.error('No se pudo iniciar sesión o la respuesta no contiene los datos esperados.');
    }

    router.push('/dashboard');
  } catch (error: any) {
    errorMessage.value = error.message || 'Ocurrió un error durante el inicio de sesión.';
    console.error(errorMessage.value);
  } finally {
    isLoading.value = false; // Stop loading
  }

};

</script>


<template>
  <div class="flex items-center h-screen justify-center px-6 bg-gray-200">
    <div class="w-full max-w-sm p-6 bg-white rounded-md shadow-md">
      <div class="flex items-center justify-center">
        <!-- <img src="../assets/gpt_lawyer.png" width="210px"> -->
      </div>

      <form class="mt-4" @submit.prevent="userSignUp">
        <label class="block">
          <span class="text-sm text-gray-700">Nombre</span>
          <input v-model="signUpDetails.name" type="text"
            class="block w-full mt-1 border-gray-200 rounded-md focus:border-indigo-600 focus:ring focus:ring-opacity-40 focus:ring-indigo-500">
        </label>

        <label class="block">
          <span class="text-sm text-gray-700">Usuario</span>
          <input v-model="signUpDetails.username" type="text"
            class="block w-full mt-1 border-gray-200 rounded-md focus:border-indigo-600 focus:ring focus:ring-opacity-40 focus:ring-indigo-500">
        </label> 

        <label class="block mt-3">
          <span class="text-sm text-gray-700">Password</span>
          <input v-model="signUpDetails.password" type="password"
            class="block w-full mt-1 border-gray-200 rounded-md focus:border-indigo-600 focus:ring focus:ring-opacity-40 focus:ring-indigo-500">
        </label>

        <label class="block mt-3">
          <span class="text-sm text-gray-700">Email</span>
          <input v-model="signUpDetails.email" type="email"
            class="block w-full mt-1 border-gray-200 rounded-md focus:border-indigo-600 focus:ring focus:ring-opacity-40 focus:ring-indigo-500">
        </label>

        <div class="mt-6">
          <button type="submit"
            class="w-full px-4 py-2 text-sm text-center text-white bg-indigo-600 rounded-md focus:outline-none hover:bg-indigo-500"
            :disabled="isLoading">
            <span v-if="!isLoading">Registrarse</span>
            <span v-else>Cargando...</span>
          </button>
        </div>
      </form>

      <div v-if="errorMessage" class="mt-3 text-red-500 text-sm">
        {{ errorMessage }}
      </div>

      <div class="text-right mt-4">
        <small>¿Ya tienes una cuenta?
          <router-link to="/" class="text-teal-500 hover:underline">Inicia sesión</router-link>
        </small>
      </div>


    </div>
  </div>
</template>