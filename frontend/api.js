var arr = [
  "https://res.cloudinary.com/dfbhak9sx/video/upload/v1752085838/vid1_elnafm.mp4",
  "https://res.cloudinary.com/dfbhak9sx/video/upload/v1752085808/vid4_vfnec5.mp4",
  "https://res.cloudinary.com/dfbhak9sx/video/upload/v1752085574/vid3_r6nwgj.mp4",
  "https://res.cloudinary.com/dfbhak9sx/video/upload/v1752085844/vid2_mnejsc.mp4",
  "https://res.cloudinary.com/dfbhak9sx/video/upload/v1752086316/vid5_pzhrov.mp4",
  "https://res.cloudinary.com/dfbhak9sx/video/upload/v1752085851/vid6_reukbc.mp4",
  "https://media.istockphoto.com/id/1154528876/video/at-old-street-in-trastevere-rome-italy-trastevere-is-rione-of-rome-on-the-west-bank-of-the.mp4?s=mp4-640x640-is&k=20&c=1qV5Rlei9Eqh_-g1ZGExxVkGSMPsYwiCvrSKRbwNHfM=",
  "https://media.istockphoto.com/id/1427993108/video/lights-and-candles-retro-light-bulbs-rustic-style-light-decoration-vintage-lights-wedding.mp4?s=mp4-640x640-is&k=20&c=QXLhCGRiAAdIzes71p6m2zJkmwRquUKCj8AjWAkcjXQ=",
];
var audio = new Audio();
audio.src = "bgAudio.mp3";
var feedback = new Audio();
var completed = new Audio();
completed.src = "feedback.mp3";
feedback.src = "completed.mp3";
feedback.volume = 0.08;
completed.volume = 0.08;
