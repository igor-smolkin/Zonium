import { createRouter, createWebHistory } from 'vue-router'
import SessionsView from '../components/views/SessionsView.vue'
import ProxiesView from '../components/views/ProxiesView.vue'
import AuthForm from '../components/auth/AuthForm.vue'

const routes = [
    { path: '/', component: SessionsView },
    { path: '/proxies', component: ProxiesView },
    { path: '/auth', component: AuthForm }
]

const router = createRouter({
    history: createWebHistory(),
    routes,
})

export default router

