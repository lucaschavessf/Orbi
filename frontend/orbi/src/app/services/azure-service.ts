import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { firstValueFrom } from 'rxjs';
import { environment } from '../../environments/environments';

@Injectable({
  providedIn: 'root'
})
export class AzureService {

  private http = inject(HttpClient);
  private apiUrl = `${environment.apiUrl}/files/sas-token`;

  async uploadFile(containerName: string, file: File): Promise<string> {
    const fileName = `${crypto.randomUUID()}-${file.name}`;

    const sasData: any = await firstValueFrom(
      this.http.get(`${this.apiUrl}?container=${containerName}&filename=${fileName}`)
    );

    const uploadUrl = sasData.uploadUrl;
    const finalUrl = sasData.fileUrl;

    await fetch(uploadUrl, {
      method: 'PUT',
      headers: {
        "x-ms-blob-type": "BlockBlob"
      },
      body: file
    });

    return finalUrl;
  }

  generateReadUrl(containerName: string, fileName: string) {
    const encodedFileName = encodeURIComponent(fileName);
    return this.http.get(`${environment.apiUrl}/files/generate-read-url?container=${containerName}&filename=${encodedFileName}`);
  }

}
