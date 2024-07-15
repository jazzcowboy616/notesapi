Details explaining the choice of framework/db/ any 3rd party tools.
instructions on how to run your code and run the tests.
Any necessary setup files or scripts to run your code locally or in a test environment.

Authentication Endpoints
POST /api/auth/signup: create a new user account.x
POST /api/auth/login: log in to an existing user account and receive an access token.x

Note Endpoints
GET /api/notes: get a list of all notes for the authenticated user.x
GET /api/notes/:id: get a note by ID for the authenticated user.x
POST /api/notes: create a new note for the authenticated user.x
PUT /api/notes/:id: update an existing note by ID for the authenticated user.x
DELETE /api/notes/:id: delete a note by ID for the authenticated user.x
POST /api/notes/:id/share: share a note with another user for the authenticated user.x
GET /api/search?q=:query: search for notes based on keywords for the authenticated user.x