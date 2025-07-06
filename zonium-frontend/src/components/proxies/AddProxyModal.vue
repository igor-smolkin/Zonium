<template>
  <div class="modal-overlay" @click.self="$emit('cancel')">
    <div class="modal-content">
      <div class="modal-header">
        <h2>Добавить прокси</h2>
        <button class="close-btn" @click="$emit('cancel')">×</button>
      </div>

      <form @submit.prevent="submit" class="modal-form">
        <input v-model="form.name" placeholder="Name" required />
        <input v-model="form.host" placeholder="Host" required />
        <input v-model.number="form.port" placeholder="Port" type="number" required />
        <input v-model="form.username" placeholder="Username" />
        <input v-model="form.password" placeholder="Password" type="password" />

        <div class="form-buttons">
          <button type="submit" class="submit-btn">Сохранить</button>
          <button type="button" class="cancel-btn" @click="$emit('cancel')">Отмена</button>
        </div>
      </form>
    </div>
  </div>
</template>

<style scoped>
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100vw;
  height: 100vh;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 9999;
}

.modal-content {
  background: white;
  padding: 20px 25px;
  border-radius: 8px;
  max-width: 400px;
  width: 90%;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.3);
}

.modal-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 1rem;
}

.close-btn {
  background: none;
  font-size: 28px;
  cursor: pointer;
  line-height: 1;
}

.modal-form {
  display: flex;
  flex-direction: column;
  gap: 1rem;
}

.modal-form select {
  width: 100%;
  padding: 8px 12px;
  margin-bottom: 1rem;
  border-radius: 4px;
  border: 1px solid #ccc;
  box-sizing: border-box;
}

.form-buttons {
  display: flex;
  gap: 10px;
  justify-content: flex-end;
}

.submit-btn {
  background-color: #42b983;
  color: white;
  border: none;
  padding: 8px 16px;
  border-radius: 4px;
  cursor: pointer;
}

.submit-btn:hover {
  background-color: #369c6f;
}

.cancel-btn {
  background-color: #e0e0e0;
  border: none;
  padding: 8px 16px;
  border-radius: 4px;
  cursor: pointer;
}

.cancel-btn:hover {
  background-color: #bdbdbd;
}

.modal-form input {
  width: 100%;
  padding: 8px 12px;
  border-radius: 4px;
  border: 1px solid #ccc;
  box-sizing: border-box;
  font-size: 14px;
  transition: border-color 0.2s ease;
}

.modal-form input:focus {
  border-color: #42b983;
  outline: none;
}
</style>

<script setup>
import { reactive } from 'vue'
const emit = defineEmits(['created', 'cancel'])

const form = reactive({
  name: '', host: '', port: null, username: '', password: '',
})

async function submit() {
  await fetch('http://localhost:8080/api/proxy', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    credentials: "include",
    body: JSON.stringify(form),
  })
  emit('created')
  Object.assign(form, { name: '', host: '', port: null, username: '', password: '' })
}
</script>