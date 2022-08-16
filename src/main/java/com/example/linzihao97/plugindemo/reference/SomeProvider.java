package com.example.linzihao97.plugindemo.reference;

import com.example.linzihao97.plugindemo.utils.IconUtils;
import com.example.linzihao97.plugindemo.utils.PsiUtils;
import com.intellij.codeInsight.daemon.RelatedItemLineMarkerInfo;
import com.intellij.codeInsight.daemon.RelatedItemLineMarkerProvider;
import com.intellij.codeInsight.navigation.NavigationGutterIconBuilder;
import com.intellij.openapi.editor.markup.GutterIconRenderer;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.util.Query;
import org.apache.commons.collections.CollectionUtils;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class SomeProvider extends RelatedItemLineMarkerProvider {
    /**
     * @param element 跳转的起点
     * @param result  把需要跳转的, 构建个 RelatedItemLineMarkerInfo 放到 result 里面
     */
    @Override
    protected void collectNavigationMarkers(@NotNull PsiElement element, @NotNull Collection<? super RelatedItemLineMarkerInfo<?>> result) {
        if (!(element instanceof PsiClass)) {
            return;
        }

        List<PsiElement> target = acquireTarget(element);

        if (CollectionUtils.isEmpty(target)) {
            return;
        }

        NavigationGutterIconBuilder<PsiElement> builder = NavigationGutterIconBuilder
                .create(IconUtils.JAVA_TO_XML_ICON)
                .setAlignment(GutterIconRenderer.Alignment.CENTER)
                .setTargets(target)
                .setTooltipTitle("Navigation to Target in Mapper Xml");

        PsiNameIdentifierOwner identifierOwner = (PsiNameIdentifierOwner) element;
        if (Objects.isNull(identifierOwner.getNameIdentifier())) {
            return;
        }

        result.add(builder.createLineMarkerInfo(identifierOwner.getNameIdentifier()));
    }

    /**
     * 获取跳转目标
     *
     * @param element {@link PsiElement}
     * @return 跳转目标列表
     */
    private List<PsiElement> acquireTarget(PsiElement element) {
        Project project = element.getProject();
        GlobalSearchScope scope = GlobalSearchScope.projectScope(project);
        JavaPsiFacade javaPsiFacade = JavaPsiFacade.getInstance(project);
        PsiClass aClass = javaPsiFacade.findClass("repl.SomeDTO", scope);

        Query<PsiMethod> methods = PsiUtils.findParameterUsages(aClass)
                .filtering(method ->
                        Arrays.stream(method.getModifierList().getAnnotations())
                                .anyMatch(psiAnnotation ->
                                        psiAnnotation.hasQualifiedName("com.shopcider.plutus.component.eventbus.CiderSubscribe") ||
                                                psiAnnotation.hasQualifiedName("org.springframework.lang.NonNull")));

        return new ArrayList<>(methods.findAll());
    }
}
