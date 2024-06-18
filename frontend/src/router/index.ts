import { createRouter, createWebHistory } from 'vue-router'
import Login from '@/views/Login.vue'
import SignUp from '@/views/SignUp.vue'
import Dashboard from '@/views/Dashboard.vue'
import Leads from '@/views/Leads.vue'
import ShowLead from '@/views/ShowLead.vue'
import Contacts from '@/views/Contacts.vue'
import Users from '@/views/Users.vue'
import Products from '@/views/Products.vue'
import SalesDetails from '@/views/SalesDetails.vue'
import Forms from '@/views/Forms.vue'
import Card from '@/views/Card.vue'
import Tables from '@/views/Tables.vue'
import UIElements from '@/views/UIElements.vue'
import Modal from '@/views/Modal.vue'
import { useUserStore } from '@/stores/user'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/login',
      name: 'Login',
      component: Login,
      meta: { layout: 'empty', requiresAuth: false },
    },
    {
      path: '/signup',
      name: 'SignUp',
      component: SignUp,
      meta: { layout: 'empty', requiresAuth: false },
    },
    {
      path: '/',
      name: 'Dashboard',
      component: Dashboard,
      meta: { requiresAuth: true },
    },
    {
      path: '/leads',
      name: 'Leads',
      component: Leads,
      meta: { requiresAuth: true },
    },
    {
      path: '/leads/:id',
      name: 'ShowLead',
      component: ShowLead,
      meta: { requiresAuth: true },
    },
    {
      path: '/contacts',
      name: 'Contactos',
      component: Contacts,
      meta: { requiresAuth: true },
    },
    {
      path: '/users',
      name: 'Usuarios',
      component: Users,
      meta: { requiresAuth: true },
    },
    {
      path: '/products',
      name: 'Productos',
      component: Products,
      meta: { requiresAuth: true },
    },
    {
      path: '/sales_details',
      name: 'Ventas',
      component: SalesDetails,
      meta: { requiresAuth: true },
    },
    {
      path: '/forms',
      name: 'Forms',
      component: Forms,
      meta: { requiresAuth: false }
    },
    {
      path: '/cards',
      name: 'Cards',
      component: Card,
      meta: { requiresAuth: false }
    },
    {
      path: '/tables',
      name: 'Tables',
      component: Tables,
      meta: { requiresAuth: false }
    },
    {
      path: '/ui-elements',
      name: 'UIElements',
      component: UIElements,
      meta: { requiresAuth: false }
    },
    {
      path: '/modal',
      name: 'Modal',
      component: Modal,
      meta: { requiresAuth: false }
    },

    {
      path: '/blank',
      name: 'blank',
      component: () => import('../views/AboutView.vue')
    }
  ]
})

router.beforeEach((to, from, next) => {
  const userStore = useUserStore();

  if (to.meta.requiresAuth && !userStore.getToken) {
    next({ name: 'Login' });
  } else {
    next();
  }
});

export default router
