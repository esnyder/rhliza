#!/usr/bin/env python
# DON'T apt-get install python-twitter
# using https://pypi.python.org/pypi/twitter
import os, sys, re, time
#import twitter as tw
from twitter import *
from nltk.chat.eliza import eliza_chatbot as bot

# Set these in your .bashrc or something.
# Find them on the twitter dev web site for the '@rhlize' user, weak password 't*'
at =os.getenv('RHLIZA_ACCESS_TOKEN')
ats=os.getenv('RHLIZA_ACCESS_TOKEN_SECRET')
ck =os.getenv('RHLIZA_CONSUMER_KEY')
cs =os.getenv('RHLIZA_CONSUMER_SECRET')

#print at, ats, ck, cs
t = Twitter(auth=OAuth(at, ats, ck, cs))

# map of userid -> tweetid
# of the last message we have responded to from any user
lastmsgs = dict()

# don't respond to any messages older than our current startup time
#starttime = time.now()

while True:
    print "getting user_timeline"
    #x = t.statuses.mentions()
    #x = t.statuses.home_timeline()
    x = t.search.tweets(q="@roguehacklab")
    
    print "got response x, len(x):", len(x), ", type(x):", type(x)
    print dir(x)
    #print x
    #print x[0].keys()
    for r in x.values():
        print x.keys()
        for s in x['statuses']:
            print s['text']
    #print
    #print dir(bot)
    print

    #print x[0]['user']['screen_name'], x[0]['text']
    # The first 'tweet' in the timeline
    # x[0]

    #print "reply with: ", bot.respond(x[0]['text'])
    break # don't actually keep doing this right now

    time.sleep(60)
