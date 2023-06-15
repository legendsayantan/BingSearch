import random
import string
import subprocess
import time

# Generate 30 random words
words = []
for i in range(34):
    # Generate a random word of length between 5 and 10
    word = ''.join(random.choice(string.ascii_lowercase) for j in range(random.randint(5, 10)))
    words.append(word)

# put your path to edge browser in the variable
edge_path = r"C:\Users\Admin\AppData\Local\Microsoft\Edge\Application\msedge.exe"  # Replace with the actual Chrome executable path
for word in words:
    search_url = f'https://www.bing.com/search?q={word}'
    subprocess.Popen([edge_path, search_url])
    time.sleep(2)
