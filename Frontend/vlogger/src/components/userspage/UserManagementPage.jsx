import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import UserService from '../service/UserService';

function UserManagementPage() {
  const [users, setUsers] = useState([]);

  useEffect(() => {
    fetchUsers();
  }, []);

  const fetchUsers = async () => {
    try {
      const token = localStorage.getItem('token'); // Retrieve the token from localStorage
      const response = await UserService.getAllUsers(token);
      setUsers(response.usersList); // Assuming the list of users is under the key 'ourUsersList'
    } catch (error) {
      console.error('Error fetching users:', error);
    }
  };

  const deleteUser = async (userId) => {
    try {
      const confirmDelete = window.confirm('Are you sure you want to delete this user?');

      const token = localStorage.getItem('token');
      if (confirmDelete) {
        await UserService.deleteUser(userId, token);
        await fetchUsers();
      }
    } catch (error) {
      console.error('Error deleting user:', error);
    }
  };

  return (
    <div className="user-management-container">
      <h2>Users Management Page</h2>
      <button className='reg-button'> <Link to="/register">Add User</Link></button>
      <table>
        <thead>
        <tr>
          <th>ID</th>
          <th>Name</th>
          <th>Email</th>
          <th>Username</th>
          <th>Actions</th>
        </tr>
        </thead>
        <tbody>
        {users
            .filter(user => (user.disable === 0))
            .map(user => (
                <tr key={user.id}>
                  <td>{user.id}</td>
                  <td>{user.name}</td>
                  <td>{user.email}</td>
                  <td>{user.username}</td>
                  <td>
                    {user.email !== 'admin123@admin.vlog' && (
                        <button className='delete-button' onClick={() => deleteUser(user.id)}>
                          Delete
                        </button>
                    )}
                    <button>
                      <Link to={`/update-user/${user.id}`}>
                        Update
                      </Link>
                    </button>
                  </td>
                </tr>
            ))}
        </tbody>
      </table>
    </div>
  );
}

export default UserManagementPage;