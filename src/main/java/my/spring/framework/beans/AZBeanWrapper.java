package my.spring.framework.beans;

/**
 * Description:
 *
 * @author Linzr
 * @version V1.0.0
 * @date 2021/3/3 3:01 下午
 * @since V1.0.0
 */
public class AZBeanWrapper {

    private Object wrappedInstance;
    private Class<?> wrappedClass;

    public AZBeanWrapper(Object wrappedInstance) {
        this.wrappedInstance = wrappedInstance;
        this.wrappedClass = wrappedInstance.getClass();
    }

    public Object getWrappedInstance() {
        return this.wrappedInstance;
    }

    public Class<?> getWrappedClass() {
        return wrappedInstance.getClass();
    }
}
