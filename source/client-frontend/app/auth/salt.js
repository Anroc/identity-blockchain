import btoa from 'btoa';

/*
export default function(name, password) {
  return btoa(encodeURIComponent(seed).replace(/%([0-9A-F]{2})/g,
    function toSolidBytes (match, p1) {
      return String.fromCharCode('0x' + p1)
    }))
}
 */

/**
 * Generate 16 bytes salt for bcrypt by seed. Should return the same salt for the same seed.
 * @param  {string} seed The seed for salt
 */
export default function (seed) {
  const bytes = [];

  for (let i = 0, l = seed.length; i < l; i += 1) {
    bytes.push(seed.charCodeAt(i));
  }

  // Salt must be 16 bytes
  while (bytes.length < 16) {
    bytes.push(0);
  }

  // Convert byte array to base64 string
  const salt = btoa(String.fromCharCode.apply(String, bytes.slice(0, 16)));

  // Adding header for bcrypt. Fake 10 rounds.
  return `$2a$10$${salt}`;
}
