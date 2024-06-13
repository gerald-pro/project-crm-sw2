import './assets/main.css'
import App from './App.vue'
import { createApp, h, provide } from 'vue'
import { ApolloClient, createHttpLink, HttpLink, InMemoryCache } from '@apollo/client/core';
import { DefaultApolloClient } from '@vue/apollo-composable';
import router from './router';
import DashboardLayout from './components/DashboardLayout.vue';
import EmptyLayout from './components/EmptyLayout.vue';
import { FontAwesomeIcon } from "@fortawesome/vue-fontawesome";
import { library } from '@fortawesome/fontawesome-svg-core'
import { faUserSecret, faDashboard, faUsers, faTable, faMagnifyingGlassDollar, faAddressBook, faX, faPen, faTrash, faEye } from '@fortawesome/free-solid-svg-icons'
import { createPinia } from 'pinia';
import Vue3Toastify, { type ToastContainerOptions } from 'vue3-toastify';

library.add(faUserSecret, faDashboard, faUsers, faTable, faMagnifyingGlassDollar, faAddressBook, faX, faPen, faTrash, faEye)

const httpLink = new HttpLink({
    uri: "http://127.0.0.1:9091/graphql",
    fetch: (uri, options = {}) => {
        // Asegúrate de que options.headers sea una instancia de Headers
        if (!(options.headers instanceof Headers)) {
          options.headers = new Headers(options.headers);
        }
    
        const token = localStorage.getItem('token');
        if (token) {
          // Ahora puedes usar el método 'set' de forma segura
          options.headers.set('Authorization', `Bearer ${token}`);
        }
        return fetch(uri, options);
      },
});

// Cache implementation
const cache = new InMemoryCache();

// Create the apollo client
export const apolloClient = new ApolloClient({
    link: httpLink,
    cache: cache,
});


const app = createApp({
    setup() {
        provide(DefaultApolloClient, apolloClient)
    },
    render: () => h(App),
})

app.component('DefaultLayout', DashboardLayout)
    .component('EmptyLayout', EmptyLayout)
    .component("font-awesome-icon", FontAwesomeIcon)

app.use(Vue3Toastify, {
    autoClose: 3000,
    position: "top-center",
} as ToastContainerOptions);

app.use(router).use(createPinia()).mount('#app');
