import random
import string
import webbrowser
import time

# Generate 30 random words
words = []
for i in range(34):
    # Generate a random word of length between 5 and 10
    word = ''.join(random.choice(string.ascii_lowercase) for j in range(random.randint(5, 10)))
    words.append(word)

# Search each word on Bing
for word in words:
    search_url = f'https://www.bing.com/search?q={word}'
    webbrowser.open_new_tab(search_url)
    time.sleep(2)
