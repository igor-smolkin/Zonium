<script lang="ts" setup>
import {ref} from 'vue'
import {useRouter} from 'vue-router'

const router = useRouter()

const isLogin = ref(true)

const email = ref('')
const password = ref('')
const confirmPassword = ref('')

const errorMessage = ref('')

async function register() {
  if (password.value !== confirmPassword.value) {
    errorMessage.value = 'Пароли не совпадают'
    return
  }
  errorMessage.value = ''
  try {
    const response = await fetch('http://localhost:8081/auth/register', {
      method: 'POST',
      headers: {'Content-Type': 'application/json'},
      body: JSON.stringify({
        email: email.value,
        password: password.value,
      }),
    })

    if (!response.ok) {
      const data = await response.json()
      errorMessage.value = data.message || 'Ошибка регистрации'
      return
    }

    isLogin.value = true
    email.value = ''
    password.value = ''
    confirmPassword.value = ''
    errorMessage.value = ''
  } catch (e) {
    errorMessage.value = 'Ошибка сети'
  }
}

async function login() {
  errorMessage.value = ''
  try {
    const response = await fetch('http://localhost:8081/auth/login', {
      method: 'POST',
      headers: {'Content-Type': 'application/json'},
      body: JSON.stringify({
        email: email.value,
        password: password.value,
      }),
    })

    if (!response.ok) {
      const data = await response.json()
      errorMessage.value = data.message || 'Ошибка входа'
      return
    }

    router.push('/')
  } catch (e) {
    errorMessage.value = 'Ошибка сети'
  }
}

function toggleForm() {
  errorMessage.value = ''
  email.value = ''
  password.value = ''
  confirmPassword.value = ''
  isLogin.value = !isLogin.value
}
</script>

<template>
  <div class="auth-form">
    <h2>{{ isLogin ? 'Вход' : 'Регистрация' }}</h2>

    <form @submit.prevent="isLogin ? login() : register()">
      <div>
        <label for="email">Email</label>
        <input id="email" type="email" v-model="email" required/>
      </div>

      <div>
        <label for="password">Пароль</label>
        <input id="password" type="password" v-model="password" required/>
      </div>

      <div v-if="!isLogin">
        <label for="confirmPassword">Подтвердите пароль</label>
        <input id="confirmPassword" type="password" v-model="confirmPassword" required/>
      </div>

      <p v-if="errorMessage" style="color: red;">{{ errorMessage }}</p>

      <button type="submit">{{ isLogin ? 'Войти' : 'Зарегистрироваться' }}</button>
    </form>

    <p>
      <a href="#" @click.prevent="toggleForm">
        {{ isLogin ? 'Нет аккаунта? Зарегистрироваться' : 'Уже есть аккаунт? Войти' }}
      </a>
    </p>
  </div>
</template>

<style scoped>
.auth-form {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);

  max-width: 300px;
  width: 90vw;
  margin: auto;
  padding: 1rem;
  border: 1px solid #ccc;
  border-radius: 8px;
}

.auth-form div {
  margin-bottom: 0.5rem;
}

.auth-form label {
  display: block;
  margin-bottom: 0.2rem;
}

.auth-form input {
  width: 100%;
  padding: 0.4rem;
  box-sizing: border-box;
}

.auth-form button {
  width: 100%;
  padding: 0.6rem;
  background: #42b983;
  border: none;
  color: white;
  font-weight: bold;
  border-radius: 4px;
  cursor: pointer;
}

.auth-form button:hover {
  background: #369c6f;
}

.auth-form h2,
.auth-form p {
  text-align: center;
}
</style>
