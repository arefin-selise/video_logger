import React, { useState } from 'react';

function VideoUploadModal({ isOpen, onClose}) {
    const [file, setFile] = useState(null);
    const [message, setMessage] = useState("");

    const handleFileChange = (event) => {
        setFile(event.target.files[0]);
    };

    const handleUpload = async () => {
        if (!file) {
            setMessage("Please select a file to upload.");
            return;
        }

        const formData = new FormData();
        formData.append("file", file);

        try {
            const token = localStorage.getItem('token');
            const response = await fetch("http://localhost:8005/admin/upload", {
                method: "PUT",
                body: formData,
                headers: {Authorization: `Bearer ${token}`}
            });

            if (response.ok) {
                setMessage("Video uploaded successfully.");
                setFile(null);
                onClose();
            } else {
                const errorText = await response.text();
                setMessage(`Upload failed: ${errorText}`);
            }
        } catch (error) {
            console.error("Error uploading video:", error);
            setMessage("An error occurred while uploading the video.");
        }
    };

    if (!isOpen) return null;

    return (
        <div className="modal">
            <div className="modal-content">
                <h2>Upload Video</h2>
                <input type="file" onChange={handleFileChange} />
                <button className="small-button" onClick={handleUpload}>Upload</button>
                <button className="small-button" onClick={onClose}>Close</button>
                {message && <p>{message}</p>}
            </div>
        </div>
    );
}

export default VideoUploadModal;