import React, { useState } from 'react';
import UserService from '../service/UserService';
import { useNavigate } from 'react-router-dom';

function RegistrationPage() {
    const navigate = useNavigate();

    const [formData, setFormData] = useState({
        name: '',
        email: '',
        password: '',
        phone: '',
        username: ''
    });

    const handleInputChange = (e) => {
        const { name, value } = e.target;
        setFormData({ ...formData, [name]: value });
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            const token = localStorage.getItem('token');
            await UserService.register(formData, token);

            setFormData({
                email: '',
                password: '',
                name: '',
                phone: '',
                username: ''
            });
            alert('User registered successfully');
            navigate('/admin/user-management');

        } catch (error) {
            alert('An error occurred while registering user');
        }
    };

    return (
        <div className="auth-container">
            <h2>Registration</h2>
            <form onSubmit={handleSubmit}>
                <div className="form-group">
                    <label>Display Name:</label>
                    <input type="text" name="name" value={formData.name} onChange={handleInputChange}
                           placeholder="Enter display name" required/>
                </div>
                <div className="form-group">
                    <label>Username:</label>
                    <input type="text" name="username" value={formData.username} onChange={handleInputChange}
                           placeholder="Enter username" required/>
                </div>
                <div className="form-group">
                    <label>Email:</label>
                    <input type="email" name="email" value={formData.email} onChange={handleInputChange} required/>
                </div>
                <div className="form-group">
                    <label>Password:</label>
                    <input type="password" name="password" value={formData.password} onChange={handleInputChange}
                           required/>
                </div>
                <div className="form-group">
                    <label>Phone:</label>
                    <input type="number" name="phone" value={formData.phone} onChange={handleInputChange}
                           placeholder="Enter phone number" required/>
                </div>
                <button type="submit">Register</button>
            </form>
        </div>
    );
}

export default RegistrationPage;
