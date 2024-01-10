import Service from '@ember/service';
import { tracked } from '@glimmer/tracking';

export default class UserdetailsService extends Service {
  @tracked uname = '';
  @tracked profile = '';
  @tracked topic = '';
  @tracked topicID = '';
  @tracked loginID = '';

  addUser(name) {
    this.uname = name;
    console.log(this.uname);
  }
  addProfile(profile) {
    this.profile = profile;
    console.log(this.profile);
  }
  addTopic(topic) {
    this.topic = topic;
    console.log(this.topic + 'added!');
  }
  setTopicID(topicID) {
    this.topicID = topicID;
    console.log(this.topicID + 'added!');
  }
  setloginID(loginID) {
    this.loginID = loginID;
    console.log(this.loginID + 'added!');
  }
  getUname() {
    return this.uname;
  }
  getProfile() {
    return this.profile;
  }
  getTopic() {
    return this.topic;
  }

  getTopicID() {
    return this.topicID + '';
  }
  getloginID() {
    return this.loginID + '';
  }
  clearAll() {
    console.log('clearing all the data');
    this.uname = '';
    this.profile = '';
    this.topic = '';
  }
}
