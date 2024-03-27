import tokenRequest from '../ulti/tokenRequest'

class MessengerAPI {
    getConversations(options) {
        return tokenRequest.get('/conversations', options)
    }
    getOrCreateConversationByMembers(member1,member2) {
        return tokenRequest.get(`/conversations/${member1}/${member2}`)
    }
    getMessagesByConversationId(id) {
        return tokenRequest.get(`conversations/${id}/messages`)
    }
    createMessage(message) {
        console.log(message)
        return tokenRequest.post('/messages',message)
    }
}
export default new MessengerAPI()
