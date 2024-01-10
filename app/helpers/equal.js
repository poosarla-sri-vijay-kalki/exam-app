import { helper } from '@ember/component/helper';
function equal(s1,s2)
{
    if (s1===s2) 
    {
        alert("sdkjg");
        return true;   
    }
    else{
        alert("not equal");
        return false;   
    }
}
export default helper(equal);