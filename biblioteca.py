import concurrent.futures
import requests
import time

URLS = [
    'https://www.python.org/',
    'https://www.wikipedia.org/',
    'https://www.example.com/',
    'https://www.openai.com/'
]

def descargar_pagina(url):
    response = requests.get(url)
    return url, len(response.content)

def main():
    start_time = time.time()

    with concurrent.futures.ThreadPoolExecutor() as executor:
        # Ejecutar las tareas de descarga en paralelo
        resultados = {executor.submit(descargar_pagina, url): url for url in URLS}

        # Iterar sobre los resultados a medida que se completan
        for future in concurrent.futures.as_completed(resultados):
            url = resultados[future]
            try:
                # Obtener el resultado de la tarea
                data = future.result()
            except Exception as exc:
                print(f'{url} generó una excepción: {exc}')
            else:
                print(f'{url} tiene {data[1]} bytes')

    elapsed_time = time.time() - start_time
    print(f'Tiempo transcurrido: {elapsed_time} segundos')

if __name__ == '__main__':
    main()