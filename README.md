# Moovup: Because Public Transit Shouldn't Be a Mystery (But It Sometimes Is)

## Welcome to the Jungle... I Mean, Public Transit!

So, you've stumbled upon Moovup, a project dedicated to unraveling the secrets of public transit in Israel. We're talking buses, schedules, real-time updates, and the occasional existential crisis when a bus just... doesn't show up.

This project is a labor of love, frustration, and a deep-seated need to know *exactly* where the bus is at any given moment. If you've ever felt lost in a sea of bus stops and cryptic timetables, you're in the right place.

## The Mission (Should You Choose to Accept It)

Moovup aims to provide a simple and intuitive way to track public transportation in Israel. We're talking:

*   Real-time bus locations (when the stars align and the data is accurate).
*   Route information (because "Bus #42" isn't exactly helpful).
*   Departure and arrival times (with a healthy dose of "subject to change").

## The Code (Where the Magic *Mostly* Happens)

We've embraced modern Android development practices, using Kotlin, Jetpack libraries (Compose, Lifecycle), and the Retrofit networking library.
Take a look at the `RetrofitClient.kt` in the project and see how we are creating the connection to the external API.

## The Protocols: Our Arch-Nemesis (and Sometimes Friend)

Now, let's talk about the real heroes (and villains) of this story: the **Siri and GTFS protocols**.

*   **Siri (Service Interface for Real-time Information):** This is the protocol that's supposed to tell us where the buses are *right now*. It's like trying to follow a GPS signal that's been routed through a flock of pigeons. Sometimes it's spot-on; sometimes it's... well, creatively inaccurate. Understanding this protocol, which felt a little bit like interpreting ancient hieroglyphics, was an uphill battle. If you can wrap your head around this, you should probably be working for NASA.
*   **GTFS (General Transit Feed Specification):** This one's responsible for static schedule data. Think of it as the "this is how it's *supposed* to work" protocol. It's like a beautifully crafted plan that, in practice, often has to be adjusted on the fly. It was like understanding the entire life story of the public transportation of the country just to understand how to travel.

**Disclaimer:** We've done our best to wrestle these protocols into submission, but they still have minds of their own. Don't be surprised if a bus appears to be defying the laws of physics.

## Challenges (aka, Why We're Slightly Grayer)

*   **Siri's Quirks:** We've learned to speak fluent "Siri-ese," but it doesn't always speak back clearly. Parsing its responses can be a wild ride. It was like solving a puzzle where the pieces keep changing shape.
*   **Data Accuracy:** We're at the mercy of the data gods. Sometimes, it's glorious; sometimes, it's... less so. We've added extra padding around estimated times, just in case.
*   **The Ever-Changing Landscape:** Public transit is a dynamic beast. Routes change, schedules shift, and stops magically appear and disappear. We try to keep up, but sometimes, we need a minute (or a strong coffee).

## Contributing (If You're Brave Enough)

If you're feeling adventurous and want to join the quest to conquer public transit data, we welcome your help! Just be warned: you might emerge with a newfound respect for bus drivers and a slight twitch in your eye.

## Special Thanks

To the developers of Retrofit, OkHttp and Gson, thank you for making our lives 0.0001% easier (Also Gemini).

## License

This project is licensed under the [Zominom] License. Use it wisely, and may your buses always be on time (or close enough).

## Feel free to contact me via discord: ortsuk
