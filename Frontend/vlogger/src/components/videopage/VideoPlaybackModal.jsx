import React, {useState} from 'react';
import UserService from "../service/UserService";

function VideoPlaybackModal({ isOpen, onClose, videoId, fileId }) {
    const [message, setMessage] = useState("");
    const videoUrl = `http://localhost:8005/public/${fileId}`;

    const completeVideo = async () => {
        try {
            const confirmComplete = window.confirm('Are you sure you want to confirm complete this video?');

            const token = localStorage.getItem('token');
            if (confirmComplete) {
                const payload = {
                    userId: null,
                    videoId
                }
                console.log(payload);
                await UserService.setCompleteVideo(payload, token);
                setMessage("Video Completed successfully.");
            }
        } catch (error) {
            console.error('Error completing video:', error);
        }
    };

    if (!isOpen) return null;

    return (
        <div className="modal">
            <div className="modal-content">
                {message && <p>{message}</p>}
                <video width="600" controls>
                    <source src={videoUrl} type="video/mp4"/>
                    Your browser does not support the video tag.
                </video>
                <button className="small-button" onClick={completeVideo}>Complete</button>
                <button className="small-button" onClick={onClose}>Close</button>
            </div>
        </div>
    );
}

export default VideoPlaybackModal;