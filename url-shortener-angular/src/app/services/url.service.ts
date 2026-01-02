import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { UrlShortenerRequest, UrlShortenerResponse } from '../models/models';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class UrlService {
  private http = inject(HttpClient);
  private readonly API_URL = environment.apiUrl;

  generateShortUrl(data: UrlShortenerRequest): Observable<UrlShortenerResponse> {
    return this.http.post<UrlShortenerResponse>(`${this.API_URL}/api/v1/generate`, data);
  }

  getAllUrls(): Observable<UrlShortenerResponse[]> {
    return this.http.get<UrlShortenerResponse[]>(`${this.API_URL}/api/v1/get`);
  }

  deleteUrl(id: number): Observable<any> {
    return this.http.delete(`${this.API_URL}/api/v1/${id}`, {responseType:"text"});
  }

  getOriginalUrl(shortCode: string): Observable<any> {
    return this.http.get(`${this.API_URL}/${shortCode}`);
  }
}
