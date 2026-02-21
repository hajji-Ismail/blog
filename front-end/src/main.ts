import { bootstrapApplication } from '@angular/platform-browser';
import { appConfig } from './app/app.config';
import { App } from './app/app';
if (window.location.pathname.includes('//')) {
  const newPath = window.location.pathname.replace(/\/+/g, '/');
  
  const newUrl = newPath + window.location.search + window.location.hash;
  window.history.replaceState(null, '', newUrl);
}

bootstrapApplication(App, appConfig)
  .catch((err) => console.error(err));
