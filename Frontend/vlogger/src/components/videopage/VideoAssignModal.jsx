import React, {useEffect, useState} from 'react';
import UserService from "../service/UserService";

function VideoAssignModal({ isOpen, onClose, id }) {
    const [users, setUsers] = useState([]);
    const [message, setMessage] = useState("");

    useEffect(() => {
        fetchUsers();
    }, []);

    const fetchUsers = async () => {
        try {
            const token = localStorage.getItem('token');
            const response = await UserService.getAllUsers(token);
            console.log(response);
            setUsers(response.usersList);
        } catch (error) {
            console.error('Error fetching users:', error);
        }
    };

    const assignVideo = async (userId) => {
        try {
            const confirmAssign = window.confirm('Are you sure you want to assign this video?');

            const token = localStorage.getItem('token');
            if (confirmAssign) {
                const payload = {
                    userId,
                    videoId: id
                }
                console.log(payload);
                await UserService.assignVideo(payload, token);
                setMessage("Video assigned successfully.");
                await fetchUsers();
            }
        } catch (error) {
            console.error('Error assigning video:', error);
        }
    };

    if (!isOpen) return null;

    return (
        <div className="modal">
            <div className="modal-content">
                {message && <p>{message}</p>}
                <div className="user-management-container">
                    <h2>Users List</h2>
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
                            .filter(user => (user.disable === 0 && user.email !== 'admin123@admin.vlog')) // Filter out users with disable set to true
                            .map(user => (
                                <tr key={user.id}>
                                    <td>{user.id}</td>
                                    <td>{user.name}</td>
                                    <td>{user.email}</td>
                                    <td>{user.username}</td>
                                    <td>
                                        <button className="small-button" onClick={() => assignVideo(user.id)}>Assign
                                        </button>
                                    </td>
                                </tr>
                            ))}
                        </tbody>
                    </table>
                </div>
                <button className="small-button" onClick={onClose}>Close</button>
            </div>
        </div>
    );
}

export default VideoAssignModal;