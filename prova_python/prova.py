import nltk 
import json
#import emoji
import emoticons 
import slang_words as sw
from nltk.corpus import stopwords
import string
from nltk.stem import PorterStemmer
import re 
from nltk.tokenize import TweetTokenizer

stopWords = set(stopwords.words('english')).union(set(string.punctuation))

emoji_pattern = re.compile("["
        u"\U0001F600-\U0001F64F"  # emoticons
        u"\U0001F300-\U0001F5FF"  # symbols & pictographs
        u"\U0001F680-\U0001F6FF"  # transport & map symbols
        u"\U0001F1E0-\U0001F1FF"  # flags (iOS)
        u"\U00002702-\U000027B0"
        u"\U000024C2-\U0001F251"
                           "]+", flags=re.UNICODE)

def write_file(path):
    outfile = open(path, 'w', encoding="utf8")
    EmojiPos = ["prima", "seconda","terza"]
    json.dump(EmojiPos, outfile)
    outfile.close()


def read_file(path):
    infile = open(path, 'r', encoding="utf8")
    text = infile.readlines()
    infile.close()
    return text


def filter_slang_words(word):
	if (word in sw.all_slang_words):
		word = nltk.word_tokenize(sw.all_slang_words.get(word))
		return word

def filert_hashtag(word,list_hashtag):
	if (word in list_hashtag):
		list_hashtag[word] += 1
	else:
		list_hashtag[word] = 1

def filter_emoticon(word,list_emoticon):
	if (word in list_emoticon):
		list_emoticon[word] += 1
	else:
		list_emoticon[word] = 1

def filter_emoji(token_emoji,list_emoji):
	for emoji in token_emoji:
		if (emoji in list_emoji):
			list_emoji[emoji] += 1
		else:
			list_emoji[emoji] = 1

def add_words(word,list_words):
	if (word in list_words):
		list_words[word] += 1
	else:
		list_words[word] = 1

def preprocess_tweets(sentiment):
	path = "Tweets/dataset_dt_"+sentiment+"_60k.txt"
	list_words = dict()
	list_hashtag = dict()
	list_emoticon = dict()
	list_emoji = dict()
	tweets = read_file(path)
	#tweets = ["afk going -_- MESSAGE z,io; 😋pene🍟🚗😒😒 8==D 😂 😒 #fibra droga, XD #fibroga 👎 #fibra."]
	for t in tweets:
		all_tokens = t.split()
		while len(all_tokens) > 0:
			token = all_tokens.pop(0)
			if (token in emoticons.all_emoticons):
				filter_emoticon(token, list_emoticon) 
			else:
				token = token.translate(str.maketrans('', '', "[,?!.;:\/()& _+=<>'']"))#rimuovi la punteggiatura
				if (re.search(emoji_pattern, token) ): #riesce a prendere anche le emoji non separate da spazi
					all_tokens += emoji_pattern.split(token) #stacca le parole attaccate alle emoji
					token = re.sub(r"[A-Za-z]\w+", '', token) #elimina le parole attaccate alle emoji
					token = TweetTokenizer().tokenize(token)
					filter_emoji(token, list_emoji)
				elif (token in sw.all_slang_words): #normalizza le slang word
					all_tokens += (nltk.word_tokenize(sw.all_slang_words.get(token)))
				elif (token and token[0] == "#"):
					filert_hashtag(token,list_hashtag)
				elif (token == "URL" or token == "USERNAME" or token in stopWords or token == ""):
					pass
				else:
					token = PorterStemmer().stem(token.lower())
					add_words(token,list_words)

	print("parole ",list_words)
	print("hashtag ",list_hashtag)
	print("emoticon ",list_emoticon)
	print("emoji ", list_emoji)


preprocess_tweets("anger")
