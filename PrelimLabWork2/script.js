// Motivational quotes for login
const motivationalQuotes = [
    "Today's presence matters.",
    "Every moment counts, embrace today.",
    "Your journey begins with showing up.",
    "Be present, be mindful, be you.",
    "Today is a gift, that's why it's called the present.",
    "Small steps today lead to big changes tomorrow.",
    "You are exactly where you need to be.",
    "Each day is a new page in your story.",
    "Progress, not perfection, is the goal.",
    "Your presence makes a difference.",
    "Breathe in calm, breathe out chaos.",
    "Today, choose growth over comfort.",
    "Be the author of your own story.",
    "Gratitude turns what we have into enough.",
    "This moment is yours to shape."
];

// Initialize data from localStorage or create empty arrays
let users = JSON.parse(localStorage.getItem('users')) || [];
let attendanceRecords = JSON.parse(localStorage.getItem('attendanceRecords')) || [];

// Set a random motivational quote on page load
function setMotivationalQuote() {
    const randomQuote = motivationalQuotes[Math.floor(Math.random() * motivationalQuotes.length)];
    document.getElementById('motivationalText').textContent = randomQuote;
}

// Toggle password visibility
function togglePassword(inputId) {
    const input = document.getElementById(inputId);
    const toggleIcon = input.nextElementSibling;
    
    if (input.type === 'password') {
        input.type = 'text';
        toggleIcon.textContent = '🙈';
    } else {
        input.type = 'password';
        toggleIcon.textContent = '👁️';
    }
}

// Show different pages
function showPage(page) {
    // Update button states
    const buttons = document.querySelectorAll('.page-btn');
    buttons.forEach(btn => btn.classList.remove('active'));
    event.target.classList.add('active');
    
    // Hide all sections
    document.querySelectorAll('.form-section').forEach(section => {
        section.classList.remove('active');
    });
    
    // Show selected section
    if (page === 'login') {
        document.getElementById('loginSection').classList.add('active');
    } else if (page === 'register') {
        document.getElementById('registerSection').classList.add('active');
    } else if (page === 'attendance') {
        document.getElementById('attendanceSection').classList.add('active');
        displayAttendanceRecords();
    }
}

// Registration form handler
document.getElementById('registerForm').addEventListener('submit', function(e) {
    e.preventDefault();
    
    const realName = document.getElementById('regRealName').value.trim();
    const username = document.getElementById('regUsername').value.trim();
    const password = document.getElementById('regPassword').value;
    
    // Check if username already exists
    const existingUser = users.find(u => u.username === username);
    
    if (existingUser) {
        showMessage('registerMessage', 'Username already exists. Please choose another.', 'error');
        playBeep();
        return;
    }
    
    // Create new user
    const newUser = {
        realName: realName,
        username: username,
        password: password,
        createdAt: new Date().toISOString()
    };
    
    users.push(newUser);
    localStorage.setItem('users', JSON.stringify(users));
    
    showMessage('registerMessage', `Account created successfully! Welcome, ${realName}!`, 'success');
    
    // Clear form
    document.getElementById('registerForm').reset();
    
    // Switch to login page after 2 seconds
    setTimeout(() => {
        showPage('login');
        document.querySelector('.page-btn').click();
    }, 2000);
});

// Login form handler
document.getElementById('loginForm').addEventListener('submit', function(e) {
    e.preventDefault();
    
    const username = document.getElementById('loginUsername').value.trim();
    const password = document.getElementById('loginPassword').value;
    
    // Find user
    const user = users.find(u => u.username === username && u.password === password);
    
    if (user) {
        // Successful login
        const timestamp = getCurrentTimestamp();
        
        // Create attendance record
        const attendanceRecord = {
            realName: user.realName,
            username: user.username,
            timestamp: timestamp,
            date: new Date().toISOString()
        };
        
        attendanceRecords.push(attendanceRecord);
        localStorage.setItem('attendanceRecords', JSON.stringify(attendanceRecords));
        
        // Display success message
        showMessage('loginMessage', `Welcome back, ${user.realName}! ✨`, 'success');
        
        // Display timestamp
        const timestampDisplay = document.getElementById('timestampDisplay');
        timestampDisplay.textContent = `Check-in time: ${timestamp}`;
        timestampDisplay.style.display = 'block';
        
        // Clear form
        document.getElementById('loginForm').reset();
        
        // Set new motivational quote
        setMotivationalQuote();
    } else {
        // Failed login
        showMessage('loginMessage', 'Incorrect username or password. Please try again.', 'error');
        playBeep();
        
        // Hide timestamp
        document.getElementById('timestampDisplay').style.display = 'none';
    }
});

// Get current timestamp in readable format
function getCurrentTimestamp() {
    const now = new Date();
    const options = {
        month: '2-digit',
        day: '2-digit',
        year: 'numeric',
        hour: '2-digit',
        minute: '2-digit',
        second: '2-digit',
        hour12: true
    };
    return now.toLocaleString('en-US', options);
}

// Show message to user
function showMessage(elementId, message, type) {
    const messageElement = document.getElementById(elementId);
    messageElement.textContent = message;
    messageElement.className = `message ${type}`;
    messageElement.style.display = 'block';
    
    // Hide message after 5 seconds
    setTimeout(() => {
        messageElement.style.display = 'none';
    }, 5000);
}

// Play beep sound
function playBeep() {
    const beep = document.getElementById('errorBeep');
    beep.currentTime = 0;
    beep.play().catch(error => {
        console.log('Audio playback failed:', error);
    });
}

// Display attendance records
function displayAttendanceRecords() {
    const attendanceList = document.getElementById('attendanceList');
    
    if (attendanceRecords.length === 0) {
        attendanceList.innerHTML = '<p style="text-align: center; color: #5a5247; padding: 20px;">No check-in records yet.</p>';
        return;
    }
    
    // Sort by most recent first
    const sortedRecords = [...attendanceRecords].reverse();
    
    attendanceList.innerHTML = sortedRecords.map(record => `
        <div class="attendance-item">
            <strong>${record.realName}</strong> (@${record.username})<br>
            <small style="color: #8b956d;">${record.timestamp}</small>
        </div>
    `).join('');
}

// Download attendance summary
function downloadAttendance() {
    if (attendanceRecords.length === 0) {
        alert('No attendance records to download.');
        return;
    }
    
    // Create attendance summary text
    let summaryText = '═══════════════════════════════════════════\n';
    summaryText += '        DAILY CHECK-IN JOURNAL\n';
    summaryText += '        Attendance Summary Report\n';
    summaryText += '═══════════════════════════════════════════\n\n';
    summaryText += `Generated: ${getCurrentTimestamp()}\n`;
    summaryText += `Total Check-ins: ${attendanceRecords.length}\n\n`;
    summaryText += '───────────────────────────────────────────\n\n';
    
    attendanceRecords.forEach((record, index) => {
        summaryText += `${index + 1}. ${record.realName} (@${record.username})\n`;
        summaryText += `   Check-in: ${record.timestamp}\n\n`;
    });
    
    summaryText += '───────────────────────────────────────────\n';
    summaryText += 'End of Report\n';
    
    // Create blob and download
    const blob = new Blob([summaryText], { type: 'text/plain' });
    const link = document.createElement('a');
    link.href = window.URL.createObjectURL(blob);
    link.download = `attendance_summary_${new Date().toISOString().slice(0, 10)}.txt`;
    link.click();
    
    // Clean up
    window.URL.revokeObjectURL(link.href);
}

// Initialize page with motivational quote
setMotivationalQuote();