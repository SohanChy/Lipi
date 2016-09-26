# Anybody Home?

![anybodyhome](https://github.com/lasseborly/anybodyhome/blob/master/images/screenshot.png "Anybody Home?")

A simple theme for simple people with simple needs.

The focus of the theme is to use as few dependencies as possible to keep the layout simple and bloat free.

## Features
* __Blog only__ - Only a list of the 10 most recent posts and added pagination. There is no other pages than the main page and the post page.
* [__Highlight.js__](https://highlightjs.org/) - For all of you code needs.

## Getting Started
From the root of you Hugo site clone the theme into `themes/anybodyhome` by running:

`git clone https://github.com/lasseborly/anybodyhome.git themes/anybodyhome`

## Usage
To use Anybody Home? you must first, from the root of your Hugo site, run either:

`hugo -t anybodyhome`

or set in you `config.toml`.

`theme = "anybodyhome"`

## Configuration
You can set the normal Hugo site variables in your `config.toml` but there is also some custom Anybody Home? variables you can set. This is an example of a full `config.toml`.

```toml
theme = "anybodyhome"
baseurl = "https://hugosite.com"
languageCode = "en-us"
title = "Anybody Home?"

[params]
  subtitle = "A Simple Theme"
```

## Contributing

1. Fork it
2. Create your feature branch - `git checkout -b my-new-feature-or-fix`
3. Commit your changes - `git commit -am 'Add some feature-or-fix'`
4. Push to the branch - `git push origin my-new-feature-or-fix`
5. Create new Pull Request

## Extra
Take a look at my [docker setup](https://github.com/lasseborly/hugo-development) for developing with Hugo. It's, again, very simple, straight forward and open for contributions.
