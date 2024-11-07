import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import UserService from '../service/UserService';

function UpdateUser() {
  const navigate = useNavigate();
  const { userId } = useParams();

  const [userData, setUserData] = useState({
    name: '',
    email: '',
    phone: '',
    username: ''
  });

  useEffect(() => {
    fetchUserDataById(userId); // Pass the userId to fetchUserDataById
  }, [userId]); //when ever there is a chane in userId, run this

  const fetchUserDataById = async (userId) => {
    try {
      const token = localStorage.getItem('token');
      const response = await UserService.getUserById(userId, token); // Pass userId to getUserById
      const { name, email, phone, username } = response.user;
      setUserData({ name, email, phone, username });
    } catch (error) {
      console.error('Error fetching user data:', error);
    }
  };


  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setUserData((prevUserData) => ({
      ...prevUserData,
      [name]: value
    }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const confirmDelete = window.confirm('Are you sure you want to update this user?');
      if (confirmDelete) {
        const token = localStorage.getItem('token');
        await UserService.updateUser(userId, userData, token);
        navigate("/admin/user-management")
      }
    } catch (error) {
      console.error('Error updating user profile:', error);
      alert(error)
    }
  };

  return (
    <div className="auth-container">
      <h2>Update User</h2>
      <form onSubmit={handleSubmit}>
        <div className="form-group">
          <label>Display Name:</label>
          <input type="text" name="name" value={userData.name} onChange={handleInputChange}/>
        </div>
        <div className="form-group">
          <label>Username:</label>
          <input type="text" name="username" value={userData.username} onChange={handleInputChange}/>
        </div>
        <div className="form-group">
          <label>Email:</label>
          <input type="email" name="email" value={userData.email} onChange={handleInputChange}/>
        </div>
        <div className="form-group">
          <label>Phone:</label>
          <input type="number" name="phone" value={userData.phone} onChange={handleInputChange}/>
        </div>
        <button type="submit">Update</button>
      </form>
    </div>
  );
}

export default UpdateUser;
