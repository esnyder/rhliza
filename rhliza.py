#!/usr/bin/env python

import os, sys, re, time
from twitter import *
from nltk.chat.eliza import eliza_chatbot as bot

# Set these in your .bashrc or something.
# Find them on the twitter dev web site for the '@rhlize' user, weak password 't*'
t = Twitter(auth=OAuth(os.getenv('RHLIZA_ACCESS_TOKEN'), os.getenv('RHLIZA_ACCESS_TOKEN_SECRET'), 
                       os.getenv('RHLIZA_CONSUMER_KEY'), os.getenv('RHLIZA_CONSUMER_SECRET')))

# map of userid -> tweetid
# of the last message we have responded to from any user
lastmsgs = dict()

# don't respond to any messages older than our current startup time
#starttime = time.now()

while True:
    print "getting user_timeline"
    x = t.statuses.mentions()
    
    print "got response x, len(x):", len(x), ", type(x):", type(x)
    print x[0].keys()
    print
    print dir(bot)
    print

    print x[0]['user']['screen_name'], x[0]['text']
    # The first 'tweet' in the timeline
    # x[0]

    print "reply with: ", bot.respond(x[0]['text'])
    break # don't actually keep doing this right now

    time.sleep(60)
