import random
import string
import subprocess
import time

edge_path = r"C:\Users\Admin\AppData\Local\Microsoft\Edge\Application\msedge.exe"  # Replace with the actual Edge executable path
count = 30 # How many searches to make
delay_seconds = 3 # Replace with actual delay
close_tabs = True # If you want to close the tabs immediately (True/False)

words = []
for i in range(count):
    word = ''.join(random.choice(string.ascii_lowercase) for j in range(random.randint(5, 10)))
    words.append(word)

for word in words:
    search_url = f'https://www.bing.com/search?q={word}'
    edge_process = subprocess.Popen([edge_path, search_url])
    time.sleep(delay_seconds)
    if close_tabs:
        edge_process.terminate()
        edge_process.wait()
