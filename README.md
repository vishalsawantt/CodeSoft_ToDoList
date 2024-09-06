# To-Do List Android App

This is a simple and user-friendly To-Do List app developed for Android as part of my application for the Android Development Internship at Codesoft. The app allows users
to manage their daily tasks efficiently with features to add, edit, delete, and mark tasks as completed.


## Features

- **Task Management**: Add, edit, delete, and display tasks with a title, description, and scheduled time.
- **Task Completion Tracking**: The app visually distinguishes completed tasks with color changes in the task list.
- **Welcome Screen**: Displays a welcome message with the user's name fetched from SharedPreferences.
- **Congratulatory Message**: Shows a special message when all tasks are completed.
- **Date and Time Display**: Displays current date and time, which updates every minute.
- **Important Tasks Indicator**: Tracks and displays the number of important tasks separately.
- **Persistent Data Storage**: Saves tasks using `SharedPreferences` to retain data even after closing the app.


## Download

  [app-release [MConverter.eu].zip](https://github.com/user-attachments/files/16906664/app-release.MConverter.eu.zip)


## Screenshots

1. **Splach_Screen**, **Main_Activity**, and **Add_task**  
   <div style="display: flex; justify-content: space-between;">
      <img src="https://github.com/user-attachments/assets/627de64f-6e71-4a93-b67f-f2d268f511e0" alt="Login_Activity" width="200">
      <img src="https://github.com/user-attachments/assets/5897a7c9-c2dd-4927-b682-902f7c062910" alt="CreateAccount_activity" width="200">
      <img src="https://github.com/user-attachments/assets/3714905a-a536-4682-86d2-40140529ff15" alt="Home_Fragment" width="200">
   </div>
   
2. **Edit_task**, **Important_task**, and **Complete_task**  
    <div style="display: flex; justify-content: space-between;">
      <img src="https://github.com/user-attachments/assets/d176a14b-51ad-4bcd-b96a-db9e6f41772b" alt="Login_Activity" width="200">
      <img src="https://github.com/user-attachments/assets/6508c8bf-d09c-4a3b-8a83-b74c8ce60212" alt="CreateAccount_activity" width="200">
      <img src="https://github.com/user-attachments/assets/91e59d33-41a7-4bba-bbc9-edb07b0cca3f" alt="Home_Fragment" width="200">
   </div>
   
3. **congratulations message**,
   <div style="display: flex; justify-content: space-between;">
      <img src="https://github.com/user-attachments/assets/a093d925-3af6-41e7-a0af-a7e332f23f8b" alt="Login_Activity" width="200">
   </div>

   
## Technology Stack

- **Programming Language:** Java
- **IDE:** Android Studio
- **Architecture:** MVC (Model-View-Controller)
- **Data Storage:** `SharedPreferences` for task management


## Prerequisites

- Android Studio

  
## Usage
Splash Screen: On first launch, you will be asked to enter your name. This will be stored and displayed on subsequent app launches.
Adding a Task: Click the Floating Action Button (FAB) to add a new task. You can provide a title, description (optional), and time.
Editing a Task: Click the edit button next to a task to modify its details.
Deleting a Task: Click the delete button next to a task to remove it from the list.
Marking a Task as Important: Click the important icon to mark a task as important.
Removing All Tasks: Once all tasks are completed, a congratulatory message appears with the option to remove all tasks.


## Future Enhancements

- **Notifications:** Implement notifications for upcoming tasks.
- **Database Integration:** Migrate task storage from SharedPreferences to a more robust database solution like Room.
- **Customizable Themes:** Add support for multiple categories of tasks.
- **UI Enhancements:** Improve the user interface with more modern design elements.


## About Me

I am a passionate Android developer with experience in Java and Android Studio. This app demonstrates my skills in Android app development and my commitment to creating 
functional and user-friendly applications.

---

## Contact

For any questions or feedback, please feel free to reach out at [sawantvishal2001@gmail.com].




