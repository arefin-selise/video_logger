import React, { useState, useEffect } from 'react';
import UserService from '../service/UserService';
import VideoPlaybackModal from "./VideoPlaybackModal";

function AssignedVideoPage() {
    const [videos, setVideos] = useState([]);
    const [videoId, setVideoId] = useState('');
    const [fileId, setFileId] = useState('');

    const [isPlaybackModalOpen, setPlaybackModalOpen] = useState(false);

    const isViewed = (viewed) => viewed === 1;

    const openPlaybackModal = (id, fileName) => {
        setVideoId(id);
        setFileId(fileName);
        setPlaybackModalOpen(true);
    };

    const closePlaybackModal = () => {
        setPlaybackModalOpen(false);
        setFileId(null);
    };

    useEffect(() => {
        fetchVideos();
    }, []);

    const fetchVideos = async () => {
        try {
            const token = localStorage.getItem('token');
            const response = await UserService.getAssignedVideos(token);
            setVideos(response.assignedList);
        } catch (error) {
            console.error('Error fetching videos:', error);
        }
    };

    return (
        <div className="video-management-container">
            <h2>Assigned Videos</h2>
            <table>
                <thead>
                <tr>
                    <th>ID</th>
                    <th>Title</th>
                    <th>FileId</th>
                    <th>Viewed</th>
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
                            <td>{isViewed(video.viewed) ? 'true' : 'false'}</td>
                            <td>
                                <button className='reg-button' onClick={() => {
                                    openPlaybackModal(video.id, video.fileId)
                                }}>
                                    Play Video
                                </button>
                            </td>
                        </tr>
                    ))}
                </tbody>
            </table>
            {isPlaybackModalOpen && (
                <VideoPlaybackModal videoId={videoId} fileId={fileId} isOpen={isPlaybackModalOpen} onClose={closePlaybackModal}/>
            )}
        </div>
    );
}

export default AssignedVideoPage;