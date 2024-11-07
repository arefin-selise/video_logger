import React, { useState, useEffect } from 'react';
import UserService from '../service/UserService';
import VideoUploadModal from "./VideoUploadModal";
import VideoAssignModal from "./VideoAssignModal";

function VideoManagementPage() {
    const [videos, setVideos] = useState([]);
    const [videoId, setVideoId] = useState('');

    const [isAssignModalOpen, setAssignModalOpen] = useState(false);
    const [isUploadModalOpen, setUploadModalOpen] = useState(false);

    const openAssignModal = (videoId) => {
        setVideoId(videoId);
        setAssignModalOpen(true);
    };

    const closeAssignModal = () => {
        setAssignModalOpen(false);
        setVideoId(null);
    };

    const openUploadModal = () => {
        setUploadModalOpen(true);
    };

    const closeUploadModal = () => {
        setUploadModalOpen(false);
    };

    useEffect(() => {
        fetchVideos();
    }, []);

    const fetchVideos = async () => {
        try {
            const token = localStorage.getItem('token');
            const response = await UserService.getAllVideos(token);
            console.log(response);
            setVideos(response.videoList);
        } catch (error) {
            console.error('Error fetching videos:', error);
        }
    };

    return (
        <div className="video-management-container">
            <h2>Videos Management Page</h2>
            <button className='reg-button' onClick={openUploadModal}>Upload Video</button>
            <table>
                <thead>
                <tr>
                    <th>ID</th>
                    <th>Title</th>
                    <th>FileId</th>
                    <th>Actions</th>
                </tr>
                </thead>
                <tbody>
                {videos
                    .map(video => (
                        <tr key={video.id}>
                            <td>{video.id}</td>
                            <td>{video.title}</td>
                            <td>{video.fileId}</td>
                            <td>
                                <button className='reg-button' onClick={() => {
                                    openAssignModal(video.id)
                                }}>
                                    Assign
                                </button>
                            </td>
                        </tr>
                    ))}
                </tbody>
            </table>
            {isAssignModalOpen && (
                <VideoAssignModal id={videoId} isOpen={isAssignModalOpen} onClose={closeAssignModal}/>
            )}

            {isUploadModalOpen && (
                <VideoUploadModal isOpen={isUploadModalOpen} onClose={closeUploadModal}/>
            )}
        </div>
    );
}

export default VideoManagementPage;