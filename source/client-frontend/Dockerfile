# You should always specify a full version here to ensure all of your developers
# are running the same version of Node.
FROM node:7.8.0

# Override the base log level (info).
ENV NPM_CONFIG_LOGLEVEL warn

# Install and configure `serve`.
# RUN npm install -g serve
# CMD serve -s build
EXPOSE 3000

# Install all dependencies of the current project.
COPY package.json package.json
# COPY npm-shrinkwrap.json npm-shrinkwrap.json
RUN npm install npm@latest
RUN npm install
# Copy all local files into the image.
COPY . .

# RUN npm run build:dll

ENTRYPOINT npm start
# Build for production.
# RUN npm run build --production

# docker build -t asd .
# docker run -it -p 5000:5000 asd
