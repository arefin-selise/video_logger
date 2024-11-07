import React from 'react';
import { BrowserRouter, Routes, Route, Navigate } from "react-router-dom";
import Navbar from './components/common/Navbar';
import LoginPage from './components/auth/LoginPage';
import RegistrationPage from './components/auth/RegistrationPage';
import FooterComponent from './components/common/Footer';
import UserService from './components/service/UserService';
import UpdateUser from './components/userspage/UpdateUser';
import UserManagementPage from './components/userspage/UserManagementPage';
import VideoManagementPage from './components/videopage/VideoManagementPage';
import ProfilePage from './components/userspage/ProfilePage';
import VideoUploadModal from './components/videopage/VideoUploadModal';
import VideoAssignModal from './components/videopage/VideoAssignModal';
import AssignedVideoPage from './components/videopage/AssignedVideoPage';
import VideoPlaybackModal from './components/videopage/VideoPlaybackModal';

function App() {

  return (
    <BrowserRouter>
      <div className="App">
        <Navbar />
        <div className="content">
          <Routes>
            <Route exact path="/" element={<LoginPage />} />
            <Route exact path="/login" element={<LoginPage />} />
            <Route path="/profile" element={<ProfilePage />} />
            <Route path="/user/assigned-video" element={<AssignedVideoPage />} />

            {UserService.adminOnly() && (
              <>
                <Route path="/register" element={<RegistrationPage />} />
                <Route path="/admin/user-management" element={<UserManagementPage />} />
                <Route path="/admin/video-management" element={<VideoManagementPage />} />
                <Route path="/update-user/:userId" element={<UpdateUser />} />
                <Route path="/upload" element={<VideoUploadModal />} />
                <Route path="/assign" element={<VideoAssignModal />} />
                <Route path="/playback" element={<VideoPlaybackModal />} />
              </>
            )}
            <Route path="*" element={<Navigate to="/login" />} />‰
          </Routes>
        </div>
        <FooterComponent />
      </div>
    </BrowserRouter>
  );
}

export default App;
