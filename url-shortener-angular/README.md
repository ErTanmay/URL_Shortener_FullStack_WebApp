# URL Shortener - Angular 21 Frontend

A modern, feature-rich Angular 21 application for URL shortening with user authentication, JWT token management, and complete CRUD operations. Built with the latest Angular features including signals, standalone components, and the new control flow syntax.

## 🚀 Angular 21 Features

This project showcases the latest Angular 21 features:
- ✨ **Signals** - Reactive state management with Angular signals
- 🎯 **Standalone Components** - No NgModules required
- 🔄 **New Control Flow** - `@if`, `@for`, `@switch` syntax (no more *ngIf, *ngFor)
- 💉 **Inject Function** - Modern dependency injection
- 🛣️ **Functional Guards** - Route protection with `CanActivateFn`
- 🔌 **Functional Interceptors** - HTTP interceptors with `HttpInterceptorFn`
- 📦 **Application Builder** - New build system
- ⚡ **Lazy Loading** - Component-level lazy loading

## ✨ Application Features

**Authentication & Authorization**
- User registration with email verification
- OTP-based email verification (6-digit code)
- Login with JWT token (15-minute validity)
- Forgot password with OTP verification
- Profile management
- Auto-logout on token expiration
- HTTP interceptor for automatic JWT token attachment

**URL Management**
- Generate short URLs from long URLs
- View all your shortened URLs
- Copy short URLs to clipboard with one click
- Delete URLs
- Responsive table design with real-time updates

**Modern UI/UX**
- Clean, professional design with CSS variables
- Fully responsive layout (mobile-friendly)
- Loading states with spinners
- Success/error notifications with alerts
- Comprehensive form validation with helpful error messages
- Smooth animations and transitions

## 🛠️ Tech Stack

- **Angular 21** (Latest version)
- **TypeScript 5.6**
- **RxJS 7.8** for reactive programming
- **Angular Router** with lazy loading
- **Reactive Forms** for form handling
- **Signals** for state management
- **HttpClient** for API communication

## 📋 Prerequisites

Before you begin, ensure you have:
- **Node.js** (v20 or higher) - [Download](https://nodejs.org/)
- **npm** (v10 or higher)
- **Angular CLI 21** (will be installed with dependencies)

Check your versions:
```bash
node --version  # Should be v20+
npm --version   # Should be v10+
```

## 🔧 Installation

1. **Extract the project files** to your desired location

2. **Navigate to the project directory**:
```bash
cd url-shortener-angular
```

3. **Install dependencies**:
```bash
npm install
```

This will install Angular 21 and all required packages.

4. **Configure the API URL**:
   - Open `src/environments/environment.ts`
   - Update the `apiUrl` to match your backend server:
   ```typescript
   export const environment = {
     production: false,
     apiUrl: 'http://localhost:8086'  // Change this to your backend URL
   };
   ```

## 🚀 Running the Application

### Development Server

Start the development server:
```bash
npm start
```

The application will be available at `http://localhost:4200/`

The app will automatically reload when you change source files.

### Production Build

Build the project for production:
```bash
npm run build
```

The build artifacts will be stored in the `dist/url-shortener-angular` directory.

## 📁 Project Structure

```
url-shortener-angular/
├── src/
│   ├── app/
│   │   ├── components/
│   │   │   ├── register/           # User registration
│   │   │   ├── verify-otp/         # Email OTP verification
│   │   │   ├── login/              # User login
│   │   │   ├── dashboard/          # Main dashboard (URL management)
│   │   │   ├── profile/            # User profile update
│   │   │   └── forgot-password/    # Password reset
│   │   ├── services/
│   │   │   ├── auth.service.ts     # Authentication service
│   │   │   └── url.service.ts      # URL management service
│   │   ├── guards/
│   │   │   └── auth.guard.ts       # Route protection (functional guard)
│   │   ├── interceptors/
│   │   │   └── auth.interceptor.ts # HTTP interceptor for JWT (functional)
│   │   ├── models/
│   │   │   └── models.ts           # TypeScript interfaces
│   │   ├── app.component.ts        # Root component
│   │   └── app.routes.ts           # Application routes
│   ├── environments/
│   │   └── environment.ts          # Environment configuration
│   ├── index.html
│   ├── main.ts                     # Application bootstrap
│   └── styles.css                  # Global styles
├── public/                         # Static assets (Angular 21 convention)
├── angular.json                    # Angular configuration
├── package.json                    # Dependencies
├── tsconfig.json                   # TypeScript configuration
└── README.md                       # This file
```

## 🔄 User Flow

### 1. Registration Flow
1. Navigate to `/register`
2. Fill in user details (first name, last name, email, phone, username, password)
3. Password must meet requirements:
   - At least 8 characters
   - One uppercase letter
   - One lowercase letter
   - One number
   - One special character (@$!%*?&)
4. Submit the form
5. Check email for 6-digit OTP
6. Navigate to `/verify-otp` (automatic redirect)
7. Enter the OTP code
8. Verify and redirect to login

### 2. Login Flow
1. Navigate to `/login`
2. Enter username and password
3. Receive JWT token (valid for 15 minutes)
4. Automatically redirected to dashboard

### 3. Dashboard (URL Management)
1. View all your shortened URLs in a table
2. Create new short URL:
   - Enter a long URL (must start with http:// or https://)
   - Click "Shorten URL"
   - View the generated short URL
3. Copy short URLs to clipboard
4. Delete URLs you no longer need
5. Navigate to profile to update information
6. Logout when done

### 4. Profile Update
1. Click "Profile" button in dashboard
2. Fill in all required fields:
   - First Name, Last Name
   - Email, Phone
   - Username, Password
3. Save changes
4. Return to dashboard

### 5. Forgot Password Flow
1. Click "Forgot password?" on login page
2. Enter your email address
3. Check email for 6-digit verification code
4. Enter code and new password
5. Submit and login with new credentials

## 🌐 API Endpoints

The application connects to these backend endpoints:

### User Authentication
- `POST /user/register` - Register new user
- `POST /user/verify` - Verify OTP
- `POST /user/resend` - Resend OTP
- `POST /user/login` - User login (returns JWT token)
- `PUT /user/update` - Update user profile (requires JWT)
- `POST /user/forget-password/send-code` - Send password reset code
- `POST /user/forget-password/verify` - Verify and reset password

### URL Management (All require JWT token)
- `POST /api/v1/generate` - Generate short URL
- `GET /api/v1/get` - Get all user URLs
- `DELETE /api/v1/{id}` - Delete URL by ID
- `GET /{shortCode}` - Redirect to original URL (public)

## 🔒 Security Features

**JWT Token Management**
- Token stored in localStorage
- Automatically attached to API requests via HTTP interceptor
- 15-minute token validity with automatic logout
- Protected routes using functional auth guard
- Redirect to login on 401 Unauthorized errors

**Form Validation**
- Email format validation
- Phone number (10 digits) validation
- Password strength requirements
- Real-time validation feedback
- Touch-based error display

**Route Protection**
- Dashboard and Profile routes protected by auth guard
- Automatic redirect to login if not authenticated
- Session timeout handling with user notification

## 🎨 Angular 21 Code Examples

### Signals (Reactive State)
```typescript
loading = signal(false);
errorMessage = signal('');
urls = signal<UrlShortenerResponse[]>([]);

// Update signal
this.loading.set(true);

// Read signal in template
@if (loading()) {
  <div>Loading...</div>
}
```

### New Control Flow Syntax
```typescript
// Old way (*ngIf, *ngFor)
<div *ngIf="isVisible">Content</div>
<div *ngFor="let item of items">{{ item }}</div>

// New way (@if, @for)
@if (isVisible) {
  <div>Content</div>
}
@for (item of items; track item.id) {
  <div>{{ item }}</div>
}
```

### Inject Function
```typescript
export class MyComponent {
  private authService = inject(AuthService);
  private router = inject(Router);
  
  // No constructor needed!
}
```

### Functional Guard
```typescript
export const authGuard: CanActivateFn = (route, state) => {
  const authService = inject(AuthService);
  const router = inject(Router);
  
  if (authService.isAuthenticated()) {
    return true;
  }
  
  router.navigate(['/login']);
  return false;
};
```

## 🚦 Development Tips

### Hot Reload
The development server supports hot module replacement. Changes to source files automatically refresh the browser.

### TypeScript Strict Mode
The project uses TypeScript strict mode for better type safety:
- `strict: true`
- `noImplicitOverride: true`
- `noPropertyAccessFromIndexSignature: true`
- `noImplicitReturns: true`

### Standalone Components
All components are standalone (no NgModule):
```typescript
@Component({
  selector: 'app-my-component',
  standalone: true,  // ← Standalone component
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './my-component.html'
})
```

### Signals Best Practices
- Use signals for component state
- Update with `.set()` or `.update()`
- Read with function call: `mySignal()`
- Great for performance and simplicity

## 🐛 Troubleshooting

### Port Already in Use
If port 4200 is already in use:
```bash
ng serve --port 4300
```

### CORS Issues
Ensure your backend allows requests from `http://localhost:4200`

For Spring Boot:
```java
@CrossOrigin(origins = "http://localhost:4200")
```

### API Connection Refused
1. Verify backend is running
2. Check `apiUrl` in `src/environments/environment.ts`
3. Test API endpoints with Postman
4. Check firewall settings

### Module Not Found Errors
Reinstall dependencies:
```bash
rm -rf node_modules package-lock.json
npm install
```

### Build Errors
Clear Angular cache:
```bash
rm -rf .angular
npm run build
```

## 🌐 Browser Support

- Chrome (latest)
- Firefox (latest)
- Safari (latest)
- Edge (latest)

## 📝 Scripts

```bash
npm start          # Start development server
npm run build      # Build for production
npm run watch      # Build and watch for changes
ng serve --open    # Open browser automatically
```

## 🎯 Future Enhancements

Potential features for future versions:
- [ ] URL analytics dashboard
- [ ] Custom short URL aliases
- [ ] URL expiration dates
- [ ] QR code generation
- [ ] Bulk URL import/export
- [ ] URL categories and tags
- [ ] Dark mode support
- [ ] Social media sharing
- [ ] URL preview before shortening
- [ ] Click tracking and statistics

## 📚 Learning Resources

- [Angular 21 Documentation](https://angular.dev)
- [Angular Signals Guide](https://angular.dev/guide/signals)
- [New Control Flow Syntax](https://angular.dev/guide/templates/control-flow)
- [Standalone Components](https://angular.dev/guide/components/importing)

## 🤝 Contributing

Feel free to submit issues and enhancement requests!

## 📄 License

This project is for educational and demonstration purposes.

## 💬 Support

For questions or issues:
1. Check the browser console for errors
2. Verify backend is running and accessible
3. Ensure all API endpoints are working correctly
4. Review this README for solutions

---

**Built with Angular 21** 🚀

**Happy URL Shortening!** 🔗
