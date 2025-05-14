var arr=["bgVid/vid1.mp4","bgVid/vid2.mp4","bgVid/vid3.mp4"
,
"bgVid/vid4.mp4",
"bgVid/vid5.mp4",
"bgVid/vid6.mp4",
"https://media.istockphoto.com/id/1154528876/video/at-old-street-in-trastevere-rome-italy-trastevere-is-rione-of-rome-on-the-west-bank-of-the.mp4?s=mp4-640x640-is&k=20&c=1qV5Rlei9Eqh_-g1ZGExxVkGSMPsYwiCvrSKRbwNHfM=",
"https://media.istockphoto.com/id/1427993108/video/lights-and-candles-retro-light-bulbs-rustic-style-light-decoration-vintage-lights-wedding.mp4?s=mp4-640x640-is&k=20&c=QXLhCGRiAAdIzes71p6m2zJkmwRquUKCj8AjWAkcjXQ="
];
var audio = new Audio();
audio.src = "bgAudio.mp3";
var feedback=new Audio();
var completed=new Audio();
completed.src="feedback.mp3";
feedback.src = "completed.mp3";
feedback.volume=0.08;
completed.volume=0.08;