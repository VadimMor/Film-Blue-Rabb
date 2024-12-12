<script setup>
    import { reactive } from 'vue'
    import { getApi } from '@/shared/API/api'
    import { useAuthStore } from '@/store/globalElem'
    import { useRoute, useRouter } from "vue-router"
    import setCookie from '@/shared/cookie/setCookie'

    import Header from '@/components/basic/header/Header.vue'
    import Footer from '@/components/basic/footer/Footer.vue'

    import './style.css'

    const api = getApi();
    const authStore = useAuthStore();
    const route = useRoute();
    const router = useRouter()

    const objValidation = reactive({
        errValidation: false,
        errEmail: false,
        errPassword: false
    })

    const objAuth = reactive({
        email: null,
        password: null
    })



    const handlerSignIn = async () => {
        try {
            let saveUp = new Date()
            saveUp.setFullYear(saveUp.getFullYear() + 1)
            
            if (route.fullPath.slice(1).split("/")[0] == "organization") {
                const authData = await api.getAuth(objAuth.email, objAuth.password, 'organization')

                if (authData.token != null) {
                    authStore.changeAuthOrganization(true)
                    authStore.changeOrganization(authData.token)
                }

                setCookie('organization', `${objAuth.email}$>>$${objAuth.password}`, {
                    expires: saveUp
                });

                router.push('/organization/')
            } else {
                const authData = await api.getAuth(objAuth.email, objAuth.password, 'client')

                if (authData.token != null) {
                    authStore.changeAuthUser(true)
                    authStore.changeUser(authData.token)
                }

                setCookie('user', `${objAuth.email}$>>$${objAuth.password}`, {
                    expires: saveUp
                });

                router.push('/')
            }
        } catch {
            if (route.fullPath.slice(1).split("/")[0] == "organization") {
                authStore.changeAuthOrganization(false)
            } else {
                authStore.changeAuthUser(false)
            }
            objValidation.errValidation = true
        }
    }
</script>

<template>
    <Header />
    <div class="main">
        <div class="auth">
            <h2 class="auth_title">Авторизация</h2>

            <div class="auth_form">
                    <p v-if="objValidation.errValidation" style="color: red;" class="auth_form-error">Не правильный email или пароль</p>
                <div class="auth_form-item login">
                    <span>Почта:</span>
                    <input v-model="objAuth.email" type="email" placeholder="mail@mail.com">
                </div>
                <div class="auth_form-item password">
                    <span>Пароль:</span>
                    <input v-model="objAuth.password" type="password" placeholder="Qwerty12!">
                </div>
                <button @click="handlerSignIn" class="auth_form-btn">Войти</button>
                <router-link v-if="route.fullPath.slice(1).split('/')[0] != 'organization'" to="/registration" class="auth_form-btn">Зарегестрироваться</router-link>
            </div>
        </div>
    </div>
    <Footer />
</template>