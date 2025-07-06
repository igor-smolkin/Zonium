<template>
  <div class="modal-overlay" @click.self="$emit('cancel')">
    <div class="modal-content">
      <div class="modal-header">
        <h2>Создать сессию</h2>
        <button class="close-btn" @click="$emit('cancel')">×</button>
      </div>

      <form @submit.prevent="submit" class="modal-form">
        <select v-model="form.proxyId" required>
          <option disabled value="">Выберите прокси</option>
          <option v-for="proxy in proxies" :key="proxy.id" :value="proxy.id">
            {{ proxy.name }} ({{ proxy.host }}:{{ proxy.port }})
          </option>
        </select>

        <div class="form-buttons">
          <button type="submit" class="submit-btn">Создать</button>
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

</style>

<script setup>
import {reactive} from 'vue'

const { proxies } = defineProps({
  proxies: Array,
})
const emit = defineEmits(['created', 'cancel'])

const form = reactive({
  proxyId: '',
})

async function submit() {
  await fetch('http://localhost:8080/api/session', {
    method: 'POST',
    headers: {'Content-Type': 'application/json'},
    credentials: 'include',
    body: JSON.stringify(form),
  })
  emit('created')
  form.proxyId = ''
}
</script>