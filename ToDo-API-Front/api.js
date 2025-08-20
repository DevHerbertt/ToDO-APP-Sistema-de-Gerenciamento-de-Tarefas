const API_BASE_URL = 'http://localhost:8080';
const API_URL = `${API_BASE_URL}/api`;

// Utilitário: tenta converter resposta em JSON, senão devolve texto
async function parseResponse(response) {
  const text = await response.text();
  try {
    return JSON.parse(text);
  } catch (e) {
    console.error('Error parsing JSON:', e, 'Response text:', text);
    return text;
  }
}

async function handleResponse(response) {
  const data = await parseResponse(response);
  if (!response.ok) {
    const msg = typeof data === 'string' ? data : data.message || 'Request failed';
    throw new Error(msg);
  }
  return data;
}

// ===================== AUTH =====================
export async function registerUser(name, email, password) {
  try {
    const response = await fetch(`${API_URL}/users/register`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ name, email, password })
    });

    await handleResponse(response);
    return { success: true, message: 'Usuário registrado com sucesso' };
  } catch (error) {
    return { success: false, message: error.message };
  }
}

export async function loginUser(email, password) {
  try {
    const response = await fetch(`${API_URL}/users/login`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ email, password })
    });

    // ✅ PRIMEIRO VERIFIQUE SE A RESPOSTA É VÁLIDA
    if (!response.ok) {
      const errorText = await response.text();
      throw new Error(errorText || 'Login failed');
    }

    // ✅ O BACKEND RETORNA JSON, ENTÃO PARSEIE CORRETAMENTE
    const data = await response.json();
    
    if (!data.success) {
      throw new Error(data.message || 'Login failed');
    }

    // ✅ EXTRAIA O TOKEN DO JSON
    const token = data.token;
    
    if (!token) {
      throw new Error('No token received');
    }

    // ✅ DEBUG: VERIFIQUE O TOKEN
    console.log('Token received:', token.substring(0, 50) + '...');
    
    // Salva no localStorage
    localStorage.setItem('token', token);

    return { success: true, message: 'Login bem-sucedido', token };
  } catch (error) {
    console.error('Login error:', error);
    return { success: false, message: error.message };
  }
}

// ===================== ADMIN TASKS =====================
export async function getAllTasksForAdmin() {
  try {
    const token = getAuthToken();
    const response = await fetch(`${API_URL}/admin/tasks`, {
      headers: { 'Authorization': `Bearer ${token}` }
    });
    return await handleResponse(response);
  } catch (error) {
    console.error('Erro ao buscar tarefas admin:', error);
    throw error;
  }
}
export function logout() {
  localStorage.removeItem('token');
  localStorage.removeItem('user');
  window.location.href = 'login.html';
}

export function getAuthToken() {
  const token = localStorage.getItem('token');
  
  // ✅ REMOVA ASPAS SE EXISTIREM (caso esteja corrompido)
  if (token && typeof token === 'string') {
    // Remove aspas no início e fim se existirem
    let cleanToken = token;
    if (cleanToken.startsWith('"') && cleanToken.endsWith('"')) {
      cleanToken = cleanToken.slice(1, -1);
      console.log('Removed quotes from token');
    }
    if (cleanToken.startsWith("'") && cleanToken.endsWith("'")) {
      cleanToken = cleanToken.slice(1, -1);
      console.log('Removed single quotes from token');
    }
    return cleanToken;
  }
  
  return token;
}

export function isAuthenticated() {
  return !!getAuthToken();
}

// ===================== TASKS =====================
export async function fetchTasks(filters = {}) {
  try {
    const token = getAuthToken();
    let url = `${API_URL}/tasks`;
    let options = {
      headers: { 'Authorization': `Bearer ${token}` }
    };

    if (Object.keys(filters).length > 0) {
      url = `${API_URL}/tasks/filter`;
      options.method = 'POST';
      options.headers['Content-Type'] = 'application/json';
      options.body = JSON.stringify(filters);
    }

    const response = await fetch(url, options);
    return await handleResponse(response);
  } catch (error) {
    console.error('Erro ao buscar tarefas:', error);
    throw error;
  }
}

export async function createTask(taskData) {
  try {
    const token = getAuthToken();

    const response = await fetch(`${API_URL}/tasks`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${token}`
      },
      body: JSON.stringify(taskData)
    });

    return await handleResponse(response);
  } catch (error) {
    console.error('Erro ao criar tarefa:', error);
    throw error;
  }
}

export async function updateTask(taskData) {
  try {
    const token = getAuthToken();
    const response = await fetch(`${API_URL}/tasks`, {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${token}`
      },
      body: JSON.stringify(taskData)
    });
    return await handleResponse(response);
  } catch (error) {
    console.error('Erro ao atualizar tarefa:', error);
    throw error;
  }
}

export async function deleteTask(taskId) {
  try {
    const token = getAuthToken();
    const response = await fetch(`${API_URL}/tasks/${taskId}`, {
      method: 'DELETE',
      headers: { 'Authorization': `Bearer ${token}` }
    });
    await handleResponse(response);
    return true;
  } catch (error) {
    console.error('Erro ao deletar tarefa:', error);
    throw error;
  }
}

// ===================== ADMIN =====================
export async function fetchAllUsers() {
  try {
    const token = getAuthToken();
    const response = await fetch(`${API_URL}/admin/users`, {
      headers: { 'Authorization': `Bearer ${token}` }
    });
    return await handleResponse(response);
  } catch (error) {
    console.error('Erro ao buscar usuários:', error);
    throw error;
  }
}

export async function createTaskForUser(userId, taskData) {
  try {
    const token = getAuthToken();
    const response = await fetch(`${API_URL}/admin/tasks/${userId}`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${token}`
      },
      body: JSON.stringify(taskData)
    });
    return await handleResponse(response);
  } catch (error) {
    console.error('Erro ao criar tarefa para usuário:', error);
    throw error;
  }
}

export async function deleteAnyTask(taskId) {
  try {
    const token = getAuthToken();
    const response = await fetch(`${API_URL}/admin/tasks/${taskId}`, {
      method: 'DELETE',
      headers: { 'Authorization': `Bearer ${token}` }
    });
    await handleResponse(response);
    return true;
  } catch (error) {
    console.error('Erro ao deletar tarefa (admin):', error);
    throw error;
  }
}

export async function changeUserRole(userId, newRole) {
  try {
    const token = getAuthToken();
    const response = await fetch(`${API_URL}/admin/users/${userId}/role?newRole=${newRole}`, {
      method: 'PUT',
      headers: { 'Authorization': `Bearer ${token}` }
    });
    return await handleResponse(response);
  } catch (error) {
    console.error('Erro ao alterar papel do usuário:', error);
    throw error;
  }
}

export async function deleteUser(userId) {
  try {
    const token = getAuthToken();
    const response = await fetch(`${API_URL}/admin/users/${userId}`, {
      method: 'DELETE',
      headers: { 'Authorization': `Bearer ${token}` }
    });
    await handleResponse(response);
    return true;
  } catch (error) {
    console.error('Erro ao deletar usuário:', error);
    throw error;
  }
}