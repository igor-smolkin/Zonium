import { createRouter, createWebHistory } from 'vue-router'
import MainLayout from '../components/layouts/MainLayout.vue'
import AuthLayout from '../components/layouts/AuthLayout.vue'

import SessionsView from '../components/views/SessionsView.vue'
import ProxiesView from '../components/views/ProxiesView.vue'
import AuthForm from '../components/auth/AuthForm.vue'

const routes = [
    {
        path: '/',
        component: MainLayout,
        children: [
            { path: '', redirect: 'main' }, // перенаправляем корень
            { path: 'main', component: SessionsView },
            { path: 'proxies', component: ProxiesView },
        ],
    },
    {
        path: '/auth',
        component: AuthLayout,
        children: [{ path: '', component: AuthForm }],
    },
]

const router = createRouter({
    history: createWebHistory(),
    routes,
})

router.beforeEach((to, from, next) => {
    const isAuth = !!getAccessToken()

    // Маршруты, требующие авторизации
    const protectedPaths = ['/main', '/proxies']

    if (protectedPaths.some(path => to.path.startsWith(path)) && !isAuth) {
        // Если пользователь не авторизован и идёт на защищённый маршрут — редирект на /auth
        next('/auth')
    } else if (to.path.startsWith('/auth') && isAuth) {
        // Если пользователь авторизован и пытается зайти на /auth — редирект на /main
        next('/main')
    } else {
        next()
    }
})

function getAccessToken() {
    const match = document.cookie.match(new RegExp('(^| )accessToken=([^;]+)'))
    return match ? match[2] : null
}

export default router
