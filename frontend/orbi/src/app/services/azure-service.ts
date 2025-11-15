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

  async uploadFile(file: File): Promise<string> {
    const fileName = `${crypto.randomUUID()}-${file.name}`;

    const sasData: any = await firstValueFrom(
      this.http.get(`${this.apiUrl}?filename=${fileName}`)
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

  generateReadUrl(fileName: string) {
    return this.http.get(`${environment.apiUrl}/files/generate-read-url/${fileName}`);
  }

}
