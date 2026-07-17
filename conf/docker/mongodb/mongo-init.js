db.auth('admin', 'e8UGAOTJn5Swz')

db = db.getSiblingDB('multidb')

db.createUser({
  user: 'wfmtest',
  pwd: 'wfm',
  roles: [
    {
      role: 'readWrite',
      db: 'multidb',
    },
  ],
});
