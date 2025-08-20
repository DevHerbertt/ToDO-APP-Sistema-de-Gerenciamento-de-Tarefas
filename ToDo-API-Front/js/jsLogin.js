import { loginUser } from '../api.js';

document.addEventListener('DOMContentLoaded', () => {
    const form = document.getElementById('loginForm');
    const responseDiv = document.getElementById('response');

    form.addEventListener('submit', async (e) => {
        e.preventDefault();
        const email = form.email.value;
        const password = form.password.value;

        const result = await loginUser(email, password);

        if (result.success) {
            localStorage.setItem('token', result.message);
        }

        responseDiv.textContent = result.message;
        responseDiv.className = `response ${result.success ? 'success' : 'error'}`;
    });
});
