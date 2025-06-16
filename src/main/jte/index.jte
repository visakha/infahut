@import java.util.Map
@import java.util.Collection
@import com.infahud.infahut.plugins.core.Plugin

@param Map<String, Object> loginInfo
@param Collection<Plugin> plugins

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>InfaHut - Informatica Asset Browser</title>
    <link href="https://cdnjs.cloudflare.com/ajax/libs/tailwindcss/2.2.19/tailwind.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
    <style>
        .sidebar {
            transition: transform 0.3s ease-in-out;
        }
        .sidebar-closed {
            transform: translateX(-100%);
        }
        .content-expanded {
            margin-left: 0;
        }
        .gradient-bg {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
        }
        .card-hover:hover {
            transform: translateY(-2px);
            box-shadow: 0 10px 25px rgba(0,0,0,0.1);
        }
        .status-indicator {
            width: 12px;
            height: 12px;
            border-radius: 50%;
            display: inline-block;
            margin-right: 8px;
        }
        .status-online { background-color: #10b981; }
        .status-offline { background-color: #ef4444; }
        .pulse { animation: pulse 2s infinite; }
        @keyframes pulse {
            0%, 100% { opacity: 1; }
            50% { opacity: 0.5; }
        }
    </style>
</head>
<body class="bg-gray-50">
    <!-- Sidebar -->
    <div id="sidebar" class="sidebar fixed left-0 top-0 h-full w-64 bg-white shadow-lg z-50">
        <div class="gradient-bg p-6 text-white">
            <h1 class="text-2xl font-bold">InfaHut</h1>
            <p class="text-sm opacity-90">Asset Browser</p>
        </div>
        
        <nav class="mt-6">
            <ul class="space-y-2 px-4">
                <li>
                    <a href="#dashboard" class="nav-link flex items-center p-3 rounded-lg hover:bg-gray-100 transition-colors">
                        <i class="fas fa-tachometer-alt mr-3 text-gray-600"></i>
                        Dashboard
                    </a>
                </li>
                <li>
                    <a href="#login" class="nav-link flex items-center p-3 rounded-lg hover:bg-gray-100 transition-colors">
                        <i class="fas fa-sign-in-alt mr-3 text-gray-600"></i>
                        Login
                    </a>
                </li>
                <li>
                    <a href="#assets" class="nav-link flex items-center p-3 rounded-lg hover:bg-gray-100 transition-colors">
                        <i class="fas fa-folder mr-3 text-gray-600"></i>
                        Assets
                    </a>
                </li>
                <li>
                    <a href="#plugins" class="nav-link flex items-center p-3 rounded-lg hover:bg-gray-100 transition-colors">
                        <i class="fas fa-puzzle-piece mr-3 text-gray-600"></i>
                        Plugins
                    </a>
                </li>
            </ul>
        </nav>
    </div>

    <!-- Main Content -->
    <div id="main-content" class="ml-64 transition-all duration-300">
        <!-- Header -->
        <header class="bg-white shadow-sm border-b border-gray-200">
            <div class="flex items-center justify-between px-6 py-4">
                <div class="flex items-center">
                    <button id="sidebar-toggle" class="mr-4 p-2 rounded-md hover:bg-gray-100">
                        <i class="fas fa-bars text-gray-600"></i>
                    </button>
                    <h2 class="text-xl font-semibold text-gray-800">Dashboard</h2>
                </div>
                <div class="flex items-center space-x-4">
                    @if(loginInfo.get("isLoggedIn") != null && (Boolean) loginInfo.get("isLoggedIn"))
                        <span class="status-indicator status-online pulse"></span>
                        <span class="text-sm text-gray-600">Connected</span>
                    @else
                        <span class="status-indicator status-offline"></span>
                        <span class="text-sm text-gray-600">Disconnected</span>
                    @endif
                </div>
            </div>
        </header>

        <!-- Dashboard Content -->
        <main class="p-6">
            <div id="dashboard-section">
                <!-- Login Status Card -->
                <div class="bg-white rounded-lg shadow-md p-6 mb-6 card-hover transition-all duration-300">
                    <div class="flex items-center justify-between mb-4">
                        <h3 class="text-lg font-semibold text-gray-800">
                            <i class="fas fa-user-circle mr-2 text-blue-600"></i>
                            Login Status
                        </h3>
                        @if(loginInfo.get("isLoggedIn") != null && (Boolean) loginInfo.get("isLoggedIn"))
                            <span class="px-3 py-1 bg-green-100 text-green-800 rounded-full text-sm font-medium">
                                Active
                            </span>
                        @else
                            <span class="px-3 py-1 bg-red-100 text-red-800 rounded-full text-sm font-medium">
                                Inactive
                            </span>
                        @endif
                    </div>
                    
                    <div id="login-info">
                        @if(loginInfo.get("isLoggedIn") != null && (Boolean) loginInfo.get("isLoggedIn"))
                            <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
                                <div>
                                    <label class="block text-sm font-medium text-gray-600 mb-1">Session ID</label>
                                    <p class="text-sm bg-gray-50 p-2 rounded border font-mono">
                                        @if(loginInfo.get("sessionId") != null)${loginInfo.get("sessionId").toString()}@endif
                                    </p>
                                </div>
                                <div>
                                    <label class="block text-sm font-medium text-gray-600 mb-1">Username</label>
                                    <p class="text-sm text-gray-800">
                                        @if(loginInfo.get("username") != null)${loginInfo.get("username").toString()}@endif
                                    </p>
                                </div>
                                <div>
                                    <label class="block text-sm font-medium text-gray-600 mb-1">Organization</label>
                                    <p class="text-sm text-gray-800">
                                        @if(loginInfo.get("orgName") != null)${loginInfo.get("orgName").toString()}@endif
                                    </p>
                                </div>
                                <div>
                                    <label class="block text-sm font-medium text-gray-600 mb-1">Time Elapsed</label>
                                    <p class="text-sm text-gray-800" data-elapsed-time>
                                        @if(loginInfo.get("elapsedTime") != null)${loginInfo.get("elapsedTime").toString()}@endif
                                    </p>
                                </div>
                                <div class="md:col-span-2">
                                    <label class="block text-sm font-medium text-gray-600 mb-1">Base API URL</label>
                                    <p class="text-sm bg-gray-50 p-2 rounded border font-mono break-all">
                                        @if(loginInfo.get("baseApiUrl") != null)${loginInfo.get("baseApiUrl").toString()}@endif
                                    </p>
                                </div>
                            </div>
                        @else
                            <div class="text-center py-8">
                                <i class="fas fa-exclamation-circle text-4xl text-gray-400 mb-4"></i>
                                <p class="text-red-600 mb-4">Not logged in to Informatica Cloud</p>
                                <button id="login-btn" class="bg-blue-600 hover:bg-blue-700 text-white px-6 py-2 rounded-lg transition-colors">
                                    <i class="fas fa-sign-in-alt mr-2"></i>
                                    Login
                                </button>
                            </div>
                        @endif
                    </div>
                    
                    @if(loginInfo.get("isLoggedIn") != null && (Boolean) loginInfo.get("isLoggedIn"))
                        <div class="mt-4 pt-4 border-t border-gray-200 flex justify-end space-x-3">
                            <button id="refresh-btn" class="bg-green-600 hover:bg-green-700 text-white px-4 py-2 rounded-lg transition-colors">
                                <i class="fas fa-sync-alt mr-2"></i>
                                Refresh Session
                            </button>
                        </div>
                    @endif
                </div>

                <!-- Plugins Status Card -->
                <div class="bg-white rounded-lg shadow-md p-6 mb-6 card-hover transition-all duration-300">
                    <h3 class="text-lg font-semibold text-gray-800 mb-4">
                        <i class="fas fa-puzzle-piece mr-2 text-purple-600"></i>
                        Plugin Status
                    </h3>
                    
                    <div class="grid grid-cols-1 md:grid-cols-3 gap-4">
                        @if(plugins != null)
                            @for(Plugin plugin : plugins)
                                <div class="border border-gray-200 rounded-lg p-4">
                                    <div class="flex items-center justify-between mb-2">
                                        <h4 class="font-medium text-gray-800">
                                            @if(plugin.getName() != null)${plugin.getName().toString()}@else Unknown Plugin @endif
                                        </h4>
                                        @if(plugin.getState() != null && "STARTED".equals(plugin.getState().toString()))
                                            <span class="px-2 py-1 bg-green-100 text-green-800 rounded text-xs font-medium">
                                                ${plugin.getState() != null ? plugin.getState().toString() : ""}
                                            </span>
                                        @else
                                            <span class="px-2 py-1 bg-yellow-100 text-yellow-800 rounded text-xs font-medium">
                                                @if(plugin.getState() != null)${plugin.getState().toString()}@else UNKNOWN @endif
                                            </span>
                                        @endif
                                    </div>
                                    <p class="text-sm text-gray-600">
                                        Version: @if(plugin.getVersion() != null)${plugin.getVersion().toString()}@else Unknown @endif
                                    </p>
                                </div>
                            @endfor
                        @else
                            <div class="col-span-3 text-center text-gray-600">
                                No plugins available
                            </div>
                        @endif
                    </div>
                </div>

                <!-- Quick Actions Card -->
                <div class="bg-white rounded-lg shadow-md p-6 card-hover transition-all duration-300">
                    <h3 class="text-lg font-semibold text-gray-800 mb-4">
                        <i class="fas fa-lightning-bolt mr-2 text-yellow-600"></i>
                        Quick Actions
                    </h3>
                    
                    <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-4">
                        <button class="action-btn p-4 border border-gray-200 rounded-lg hover:bg-gray-50 transition-colors text-center">
                            <i class="fas fa-search text-2xl text-blue-600 mb-2"></i>
                            <p class="text-sm font-medium text-gray-800">Browse Assets</p>
                        </button>
                        
                        <button class="action-btn p-4 border border-gray-200 rounded-lg hover:bg-gray-50 transition-colors text-center">
                            <i class="fas fa-download text-2xl text-green-600 mb-2"></i>
                            <p class="text-sm font-medium text-gray-800">Export Metadata</p>
                        </button>
                        
                        <button class="action-btn p-4 border border-gray-200 rounded-lg hover:bg-gray-50 transition-colors text-center">
                            <i class="fas fa-chart-bar text-2xl text-purple-600 mb-2"></i>
                            <p class="text-sm font-medium text-gray-800">View Reports</p>
                        </button>
                        
                        <button class="action-btn p-4 border border-gray-200 rounded-lg hover:bg-gray-50 transition-colors text-center">
                            <i class="fas fa-cog text-2xl text-gray-600 mb-2"></i>
                            <p class="text-sm font-medium text-gray-800">Settings</p>
                        </button>
                    </div>
                </div>
            </div>
        </main>
    </div>

    <!-- Loading Overlay -->
    <div id="loading-overlay" class="fixed inset-0 bg-gray-900 bg-opacity-50 flex items-center justify-center z-50 hidden">
        <div class="bg-white rounded-lg p-6 flex items-center">
            <i class="fas fa-spinner fa-spin text-blue-600 mr-3 text-xl"></i>
            <span class="text-gray-800">Processing...</span>
        </div>
    </div>

    <script>
        // Sidebar toggle functionality
        const sidebarToggle = document.getElementById('sidebar-toggle');
        const sidebar = document.getElementById('sidebar');
        const mainContent = document.getElementById('main-content');
        
        sidebarToggle.addEventListener('click', () => {
            sidebar.classList.toggle('sidebar-closed');
            mainContent.classList.toggle('content-expanded');
        });

        // Login functionality
        const loginBtn = document.getElementById('login-btn');
        const refreshBtn = document.getElementById('refresh-btn');
        const loadingOverlay = document.getElementById('loading-overlay');

        function showLoading() {
            loadingOverlay.classList.remove('hidden');
        }

        function hideLoading() {
            loadingOverlay.classList.add('hidden');
        }

        function updateLoginInfo(loginInfo) {
            // Reload the page to show updated information
            window.location.reload();
        }

        if (loginBtn) {
            loginBtn.addEventListener('click', async () => {
                showLoading();
                try {
                    const response = await fetch('/api/login', {
                        method: 'POST',
                        headers: {
                            'Content-Type': 'application/json'
                        }
                    });
                    const result = await response.json();
                    
                    if (result.success) {
                        updateLoginInfo(result.loginInfo);
                    } else {
                        alert('Login failed: ' + result.message);
                    }
                } catch (error) {
                    alert('Login failed: ' + error.message);
                } finally {
                    hideLoading();
                }
            });
        }

        if (refreshBtn) {
            refreshBtn.addEventListener('click', async () => {
                showLoading();
                try {
                    const response = await fetch('/api/refresh', {
                        method: 'POST',
                        headers: {
                            'Content-Type': 'application/json'
                        }
                    });
                    const result = await response.json();
                    
                    if (result.success) {
                        updateLoginInfo(result.loginInfo);
                    } else {
                        alert('Refresh failed: ' + result.message);
                    }
                } catch (error) {
                    alert('Refresh failed: ' + error.message);
                } finally {
                    hideLoading();
                }
            });
        }

        // Auto-refresh status every 30 seconds
        setInterval(async () => {
            try {
                const response = await fetch('/api/status');
                const result = await response.json();
                
                // Update elapsed time if logged in
                const timeElement = document.querySelector('[data-elapsed-time]');
                if (timeElement && result.loginInfo.isLoggedIn) {
                    timeElement.textContent = result.loginInfo.elapsedTime;
                }
            } catch (error) {
                console.error('Failed to refresh status:', error);
            }
        }, 30000);
    </script>
</body>
</html>
