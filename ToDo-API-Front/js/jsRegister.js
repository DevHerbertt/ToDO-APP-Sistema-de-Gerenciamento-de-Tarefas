import { registerUser } from '../api.js';

document.addEventListener('DOMContentLoaded', () => {
    const form = document.getElementById('registerForm');
    const responseDiv = document.getElementById('response');

    form.addEventListener('submit', async (e) => {
        e.preventDefault();
        const name = form.name.value;
        const email = form.email.value;
        const password = form.password.value;

        const result = await registerUser(name, email, password);

        responseDiv.textContent = result.message;
        responseDiv.className = `response ${result.success ? 'success' : 'error'}`;
    });
});
