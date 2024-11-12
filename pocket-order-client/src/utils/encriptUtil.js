import CryptoJS from 'crypto-js';

export function encryptPassword(password) {
    return CryptoJS.SHA256(password).toString(CryptoJS.enc.Hex)  // 对密码进行sha236加密
}
