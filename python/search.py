import random
import string
import subprocess
import time

words = []
for i in range(30):
    word = ''.join(random.choice(string.ascii_lowercase) for j in range(random.randint(5, 10)))
    words.append(word)

edge_path = r"C:\Users\Admin\AppData\Local\Microsoft\Edge\Application\msedge.exe"  # Replace with the actual Edge executable path
for word in words:
    search_url = f'https://www.bing.com/search?q={word}'
    subprocess.Popen([edge_path, search_url])
    time.sleep(3)
