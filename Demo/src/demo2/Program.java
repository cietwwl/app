package demo2;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import demo.GetSkillList;

public class Program {
    public static void main(String[] args) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException, InstantiationException{
        Grade grade=new Grade();
        
        
		GetSkillList list = new GetSkillList();
		Field[] fs = list.getClass().getDeclaredFields();
		for (Field field : fs) {
			Type fc = field.getGenericType();
			System.out.println("fc>> " + fc.getClass());
			if (fc instanceof ParameterizedType) { // ��3������Ƿ��Ͳ���������
				ParameterizedType pt = (ParameterizedType) fc;
				System.out.println("pt>> " + pt);
				Type t = pt.getActualTypeArguments()[0];
				System.out.println("t::::"+t);
				Class genericClazz = (Class) t; // ��4�� �õ��������class���Ͷ���
				 try {
				 System.out.println( genericClazz.newInstance());
				 } catch (Exception e) {
				 e.printStackTrace();
				 }
			}
		}
        
//        Field field=Grade.class.getDeclaredField("students");
        Field field=Grade.class.getDeclaredField("students");
        if(List.class.isAssignableFrom(field.getType())){
            Type type=field.getGenericType();
            //�����ж�type �ǲ��ǲ��������͡� ��Collection<String>����һ�����������͡�
            if(type instanceof ParameterizedType){
                //��ȡ���͵����Ͳ������͡�  �����ȥ�鿴jdk�����ĵ���ParameterizedType�Ľ��͡�
                Class clazz=(Class)((ParameterizedType) type).getActualTypeArguments()[0];
                System.out.println(clazz);
                Object obj=clazz.newInstance();
                Object obj2=clazz.newInstance();
                System.out.println(obj);
                System.out.println(obj2);
            }
        }
    }
}
